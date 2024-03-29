package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HolidayDTO;
import at.qe.skeleton.external.model.location.Location;
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
    private Date today;
    private Date startDate;
    private Date endDate;
    private Date endDateMax;
    private Location currentLocation;
    private String locationQuery;
    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    /**
     *  Initializes the holiday view by retrieving todays date, todays date in one year and the location.
     */
    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        locationQuery = externalContext.getRequestParameterMap().get("location");

        this.setToday(weatherService.getToday());
        this.setOneYearFromToday(weatherService.getOneYearFromToday());
        retrieveLocation();
    }

    /**
     * Refreshes the maximum end Date for the date selection based on the start date picked
     * @param event start date picked in the Date picker
     */
    public void onStartDateSelect(SelectEvent<Date> event) {
        this.setEndDateMax(weatherService.getMaximumEndDate(event, 14));
    }

    /**
     * Sets the holiday forecast based on the date selection
     * Updates the diagram to visualize the evolution of the temperatures
     * Sets the average of the last 5 years based on the dates selected
     * @throws ApiQueryException
     */
    public void getHolidayForecast() throws ApiQueryException {
        if (this.getStartDate() != null && this.getEndDate() != null) {

            List<String> chosenDates;
            chosenDates = weatherService.getChosenDates(this.getStartDate(), this.getEndDate());
            if(currentLocation != null){
                List<HolidayDTO> holidays = weatherService.retrieveDailyHolidayForecast(this.getCurrentLocation(), chosenDates);
                this.setHolidayWeatherList(holidays);
                diagramBean.updateLineModel(this.getHolidayWeatherList(), this.getStartDate(), this.getEndDate());
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

    /**
     * sets the average of the past 5 years based on the dates selected
     * @throws ApiQueryException
     */
    public void getPastAverageForDateRange() throws ApiQueryException {
        HolidayDTO pastAvgDTO = weatherService.getPastAverageDTO(getStartDate(), getEndDate(), getCurrentLocation());

        this.setPastAverage(pastAvgDTO);
    }

    /**
     * retrieves the location based on the location initially retrieved
     */
    public void retrieveLocation(){
        try {
            currentLocation = locationService.retrieveLocation(locationQuery);
        } catch (EmptyLocationException | ApiQueryException e) {
            displayLocationWarningMessage();
            logger.error("An error occurred in holiday controller: {}", e.getMessage(), e);
        }
    }

    /**
     * Displays a warning message if there was a problem in the retrieval of the location
     */
    public void displayLocationWarningMessage() {
        String message = "An error occurred in the retrieval of the Location!";
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning:", message));
    }
    /**
     * Displays a warning message if there was a problem in the retrieval of the dates
     */
    public void displayDateWarningMessage() {
        String message = "You left at least one Date unchosen!";
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning:", message));
    }
    /**
     * Sets the date one year from today
     */
        public Date getOneYearFromToday() {
        return oneYearFromToday;
    }

    public void setOneYearFromToday(Date oneYearFromToday) {
        this.oneYearFromToday = oneYearFromToday;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDateMax() {
        return endDateMax;
    }

    public void setEndDateMax(Date endDateMax) {
        this.endDateMax = endDateMax;
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
