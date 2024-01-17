package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.LocationApiRequestService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherDataService;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.HourlyWeatherDataRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Bean class for displaying in-depth weather data retrieved by the API for a certain location.
 */

@Component
@Scope("view")
public class LocationApiDemoBean {

    private static final Logger logger = LoggerFactory.getLogger(LocationApiDemoBean.class);

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
    private Location currentLocation;
    private HourlyWeatherDTO currentWeather;
    private HourlyWeatherDTO weatherInOneHour;
    private List<HourlyWeatherDTO> hourlyWeatherList;
    private List<DailyWeatherDTO> dailyWeatherList;
    private String query_name;
    private final int LIMIT = 1;

    /**
     * Initializes the Bean.
     * Takes the query name from the URL, uses the weather service and the query_name to check if the desired location has weather data.
     * Checks if there is already exists a weather data record in the database for the desired location
     * Depending on that either retrieves the weather data from the database, or via the api.
     */
    @PostConstruct
    public void init() {
        if(getQuery_name() != null){
            String location = getQuery_name();
            setQuery_name(location.replace(" ", "_"));
        }
        if(getQuery_name()!= null) {
            try {

                List<Location> answer = this.locationApiRequestService.retrieveLocations(getQuery_name(), getLIMIT());

                // Check if the list is not empty
                if (!answer.isEmpty()) {
                    // only process first entry in List of LocationDTOs

                    Location firstLocation = answer.get(0);
                    Pageable last_eight_entries = PageRequest.of(0, 8);
                    Pageable last_fourty_eight_entries = PageRequest.of(0, 2);
                    List<DailyWeatherData> latestData = dailyWeatherDataRepository.findLatestByLocation(firstLocation.getName(), last_eight_entries);
                    List<HourlyWeatherData> latestDataHourly = hourlyWeatherDataRepository.findLatestByLocation(firstLocation.getName(), last_fourty_eight_entries);

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

                    CurrentAndForecastAnswerDTO forecastAnswer = this.weatherApiRequestService.retrieveCurrentAndForecastWeather(firstLocation.getLatitude(), firstLocation.getLongitude());

                    this.setHourlyWeatherList(forecastAnswer.hourlyWeather());
                    for(int n = 0; n < getHourlyWeatherList().size(); n++){
                        weatherDataService.saveHourlyWeatherFromDTO(hourlyWeatherList.get(n), firstLocation.getName());
                    }

                    this.setDailyWeatherList(forecastAnswer.dailyWeather());
                    for(int n = 0; n < getDailyWeatherList().size(); n++){
                        weatherDataService.saveDailyWeatherFromDTO(getDailyWeatherList().get(n), firstLocation.getName());
                    }

                } else {
                    logger.warn("The list of locations is empty.");
                }
            } catch (Exception e) {
                logger.error("error in request in locationApi", e);
                throw new RuntimeException(e);
            }
        }
    }



    public String getQuery_name() {
        return query_name;
    }

    @RequestMapping(value = "/secured/detail.xhtml", method = RequestMethod.GET)
    public void setQuery_name(@RequestParam("location") String location) {
        this.query_name = location;
    }


    public int getLIMIT() {
        return LIMIT;
    }

    public void setLocation(Location location) {
        this.currentLocation = location;
    }

    public Location getCurrentLocation() {
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

