package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.misc.DailyTemperatureAggregationDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HolidayDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HumidityDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.PrecipitationDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.services.WeatherService;
import at.qe.skeleton.internal.ui.beans.DiagramBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Scope("view")
public class HolidayController {
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private DiagramBean diagramBean;
    private List<HolidayDTO> holidayWeatherList;
    private HolidayDTO pastAverage;
    private Date oneYearFromToday;
    private LineChartModel model;
    private Date today;
    private Date start_date;
    private Date end_date;
    private Date end_date_max;
    private Location currentLocation;
    private String locationQuery;
    private static final Logger logger = LoggerFactory.getLogger(DetailedWeatherController.class);

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        locationQuery = externalContext.getRequestParameterMap().get("location");

        model = new LineChartModel();
        this.setToday(weatherService.getToday());
        this.setOneYearFromToday(weatherService.getOneYearFromToday());
        retrieveLocation(locationQuery);
    }

    public void onStartDateSelect(SelectEvent event) {
        this.setEnd_date_max(weatherService.getMaximumEndDate(event, 14));
    }

    public void getHolidayForecast() {
        if (this.getStart_date() != null && this.getEnd_date() != null) {

            List<String> chosenDates = new ArrayList<>();
            chosenDates = weatherService.getChosenDates(this.getStart_date(), this.getEnd_date());
            if(currentLocation != null){
                List<HolidayDTO> holidays = weatherService.retrieveDailyHolidayForecast(this.getCurrentLocation(), chosenDates);
                this.setHolidayWeatherList(holidays);
                int size = this.getHolidayWeatherList().size();
                diagramBean.updateLineModel(this.getHolidayWeatherList(), this.getStart_date(), this.getEnd_date());
                getPastAverageForDateRange();
            }else{
                logger.warn("current location is empty");
                displayLocationWarningMessage();
            }
        } else {

            displayDateWarningMessage();
            logger.warn("You left at least one date unchosen!");
        }

    }

    public void getPastAverageForDateRange(){
        HolidayDTO pastAvgDTO = weatherService.getPastAverageDTO(getStart_date(), getEnd_date(), getCurrentLocation());

        this.setPastAverage(pastAvgDTO);
    }
    public void retrieveLocation(String query){
        try {
            currentLocation = locationService.retrieveLocation(locationQuery);
        } catch (EmptyLocationException | ApiQueryException e) {
            displayLocationWarningMessage();
            logger.error("An error occurred in holiday controller: {}", e.getMessage(), e);
        }
    }

    public void displayLocationWarningMessage() {
        String message = "An error occurred in the retrieval of the Location!";
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning:", message));
    }
    public void displayDateWarningMessage() {
        String message = "You left at least one Date unchosen!";
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning:", message));
    }
        public Date getOneYearFromToday() {
        return oneYearFromToday;
    }

    public void setOneYearFromToday(Date oneYearFromToday) {
        this.oneYearFromToday = oneYearFromToday;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public List<HolidayDTO> getHolidayWeatherList() {
        return holidayWeatherList;
    }

    public void setHolidayWeatherList(List<HolidayDTO> holidayWeatherList) {
        this.holidayWeatherList = holidayWeatherList;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getEnd_date_max() {
        return end_date_max;
    }

    public void setEnd_date_max(Date end_date_max) {
        this.end_date_max = end_date_max;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public HolidayDTO getPastAverage() {
        return pastAverage;
    }

    public void setPastAverage(HolidayDTO pastAverage) {
        this.pastAverage = pastAverage;
    }
    public List<HolidayDTO> getPastAverageAsList() {
        if (pastAverage != null) {
            return Collections.singletonList(pastAverage);
        } else {
            return Collections.emptyList();
        }
    }
}
