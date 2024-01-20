package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HolidayDTO;
import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.services.LocationApiRequestService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherDataService;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.HourlyWeatherDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;
/**
 * Bean class for displaying in-depth weather data retrieved by the API for a certain location.
 */

@Component
@Scope("view")
public class LocationApiDemoBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationApiDemoBean.class);

    @Autowired
    private LocationApiRequestService locationApiRequestService;
    @Autowired
    private DailyWeatherDataRepository dailyWeatherDataRepository;
    @Autowired
    private HourlyWeatherDataRepository hourlyWeatherDataRepository;
    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    @Autowired
    private WeatherDataService weatherDataService;
    private LocationDTO currentLocation;
    private HourlyWeatherDTO currentWeather;
    private HourlyWeatherDTO weatherInOneHour;
    private List<HourlyWeatherDTO> hourlyWeatherList;
    private List<DailyWeatherDTO> dailyWeatherList;
    private List<HolidayDTO> holidayWeatherList;
    private Date oneYearFromToday;
    private Date start_date;
    private Date end_date;
    private Date end_date_max;
    private String query_name;
    private LineChartModel model = new LineChartModel();
    private final int LIMIT = 1;

    /**
     * Initializes the Bean.
     * Takes the query name from the URL, uses the weather service and the query_name to check if the desired location has weather data.
     * Checks if there is already exists a weather data record in the database for the desired location
     * Depending on that either retrieves the weather data from the database, or via the api.
     */
    @PostConstruct
    public void init() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        this.setOneYearFromToday(cal.getTime());

        if(getQuery_name() != null){
            String location = getQuery_name();
            setQuery_name(location.replace(" ", "_"));
        }
        if(getQuery_name()!= null) {
            try {

                List<LocationDTO> answer = this.locationApiRequestService.retrieveLocations(getQuery_name(), getLIMIT());

                // Check if the list is not empty
                if (!answer.isEmpty()) {
                    // only process first entry in List of LocationDTOs

                    LocationDTO firstLocation = answer.get(0);
                    Pageable last_eight_entries = PageRequest.of(0, 8);
                    Pageable last_fourty_eight_entries = PageRequest.of(0, 2);
                    List<DailyWeatherData> latestData = dailyWeatherDataRepository.findLatestByLocation(firstLocation.name(), last_eight_entries);
                    List<HourlyWeatherData> latestDataHourly = hourlyWeatherDataRepository.findLatestByLocation(firstLocation.name(), last_fourty_eight_entries);

                    if (!latestData.isEmpty() && !latestDataHourly.isEmpty()) {
                        DailyWeatherData latestRecord = latestData.get(0);
                        HourlyWeatherData latestRecordHourly = latestDataHourly.get(0);
                        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);

                        if (latestRecord.getAdditionTime().isAfter(oneHourAgo) && latestRecordHourly.getAdditionTime().isAfter(oneHourAgo)) {
                            this.setLocation(firstLocation);

                            ArrayList<HourlyWeatherDTO> latestHourlyWeather = new ArrayList<>();
                            for(int n = latestDataHourly.size() - 1; n >= 0 ; n--){
                                latestHourlyWeather.add(weatherDataService.convertHourlyDataToDTO(latestDataHourly.get(n)));
                            }
                            this.setHourlyWeatherList(latestHourlyWeather);

                            ArrayList<DailyWeatherDTO> latestWeather = new ArrayList<>();
                            for(int n = latestData.size() - 1; n >= 0 ; n--){
                                latestWeather.add(weatherDataService.convertDailyDataToDTO(latestData.get(n)));
                            }
                            this.setDailyWeatherList(latestWeather);
                            return;
                        }
                    }
                    this.setLocation(firstLocation);

                    CurrentAndForecastAnswerDTO forecastAnswer = this.weatherApiRequestService.retrieveCurrentAndForecastWeather(firstLocation.latitude(), firstLocation.longitude());

                    this.setHourlyWeatherList(forecastAnswer.hourlyWeather());
                    for(int n = 0; n < getHourlyWeatherList().size(); n++){
                        weatherDataService.saveHourlyWeatherFromDTO(hourlyWeatherList.get(n), firstLocation.name());
                    }

                    this.setDailyWeatherList(forecastAnswer.dailyWeather());
                    for(int n = 0; n < getDailyWeatherList().size(); n++){
                        weatherDataService.saveDailyWeatherFromDTO(getDailyWeatherList().get(n), firstLocation.name());
                    }

                } else {
                    LOGGER.warn("The list of locations is empty.");
                }
            } catch (Exception e) {
                LOGGER.error("error in request in locationApi", e);
                throw new RuntimeException(e);
            }
        }
    }

    public void onStartDateSelect(SelectEvent event) {
        Date startDate = (Date) event.getObject();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, 5);
        this.setEnd_date_max(c.getTime());
    }

    public void getHolidayForecast(){
        if(this.getStart_date() != null && this.getEnd_date() != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar start = Calendar.getInstance();
            start.setTime(this.getStart_date());
            Calendar end = Calendar.getInstance();
            end.setTime(this.getEnd_date());

            List<String> chosenDates = new ArrayList<>();
            while (!start.after(end)) {
                chosenDates.add(sdf.format(start.getTime()));
                start.add(Calendar.DATE, 1);
            }

            try{
                List<HolidayDTO> holidays = new ArrayList<>();
                for(String i : chosenDates){
                    HolidayDTO holiday = weatherApiRequestService.retrieveDailyHolidayForecast(this.currentLocation.latitude(), this.currentLocation.longitude(), i);
                    LOGGER.info(holiday.date() + " " + holiday.temperatureDTO().dayTemperature() + " " + holiday.humidityDTO().afternoon());
                    holidays.add(holiday);
                }
                this.setHolidayWeatherList(holidays);
                int size = this.getHolidayWeatherList().size();
                LOGGER.info("Size: "+size);
                updateDiagram();
            }catch (Exception e) {
                LOGGER.error("error in request in WeatherApi", e);
                throw new RuntimeException(e);
            }
        }else{
            LOGGER.warn("You left at least one date unchosen!");
        }
    }

    public void updateDiagram(){
        LineChartSeries seriesMin, seriesMax, seriesDay, seriesMorn, seriesNight, seriesEve;
        seriesMin = seriesMax = seriesDay = seriesMorn = seriesNight = seriesEve = new LineChartSeries();

        seriesMin.setLabel("Minimum Temperature");
        seriesMax.setLabel("Maximum Temperature");
        seriesDay.setLabel("Afternoon Temperature");
        seriesMorn.setLabel("Morning Temperature");
        seriesNight.setLabel("Night Temperature");
        seriesEve.setLabel("Evening Temperature");
        for (HolidayDTO holiday : this.getHolidayWeatherList()) {
            seriesMin.set(holiday.date(), holiday.temperatureDTO().minimumDailyTemperature());
            seriesMax.set(holiday.date(), holiday.temperatureDTO().maximumDailyTemperature());
            seriesDay.set(holiday.date(), holiday.temperatureDTO().dayTemperature());
            seriesMorn.set(holiday.date(), holiday.temperatureDTO().morningTemperature());
            seriesNight.set(holiday.date(), holiday.temperatureDTO().nightTemperature());
            seriesEve.set(holiday.date(), holiday.temperatureDTO().eveningTemperature());
        }

        model.addSeries(seriesMin);
        model.addSeries(seriesMax);
        model.addSeries(seriesDay);
        model.addSeries(seriesMorn);
        model.addSeries(seriesNight);
        model.addSeries(seriesEve);

    }
    public String getQuery_name() {
        return query_name;
    }

    @RequestMapping(value = "/secured/detail.xhtml", method = RequestMethod.GET)
    public void setQuery_name(@RequestParam("location") String location) {
        this.query_name = location;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date selected_date) {
        this.start_date = selected_date;
    }

    public LineChartModel getModel() {
        return model;
    }

    public void setModel(LineChartModel model) {
        this.model = model;
    }

    public Date getEnd_date_max() {
        return end_date_max;
    }

    public void setEnd_date_max(Date end_date_max) {
        this.end_date_max = end_date_max;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getOneYearFromToday() {
        return oneYearFromToday;
    }

    public void setOneYearFromToday(Date oneYearFromToday) {
        this.oneYearFromToday = oneYearFromToday;
    }

    public List<HolidayDTO> getHolidayWeatherList() {
        return holidayWeatherList;
    }

    public void setHolidayWeatherList(List<HolidayDTO> holidayWeatherList) {
        this.holidayWeatherList = holidayWeatherList;
    }

    public int getLIMIT() {
        return LIMIT;
    }

    public void setLocation(LocationDTO location) {
        this.currentLocation = location;
    }

    public LocationDTO getCurrentLocation() {
        return currentLocation;
    }

    public HourlyWeatherDTO getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(HourlyWeatherDTO currentWeather) {
        this.currentWeather = currentWeather;
    }

    public HourlyWeatherDTO getWeatherInOneHour() {
        return weatherInOneHour;
    }

    public void setWeatherInOneHour(HourlyWeatherDTO weatherInOneHour) {
        this.weatherInOneHour = weatherInOneHour;
    }

    public List<HourlyWeatherDTO> getCurrentWeatherAsList() {
        if (currentWeather != null) {
            return Collections.singletonList(currentWeather);
        } else {
            return Collections.emptyList();
        }
    }

    public List<HourlyWeatherDTO> getWeatherInOneHourAsList() {
        if (weatherInOneHour != null) {
            return Collections.singletonList(weatherInOneHour);
        } else {
            return Collections.emptyList();
        }
    }

    public List<DailyWeatherDTO> getDailyWeatherList() {
        return dailyWeatherList;
    }

    public void setDailyWeatherList(List<DailyWeatherDTO> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    public List<HourlyWeatherDTO> getHourlyWeatherList() {
        return hourlyWeatherList;
    }

    public void setHourlyWeatherList(List<HourlyWeatherDTO> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }
}

