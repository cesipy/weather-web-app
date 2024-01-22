package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.services.WeatherService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Scope("view")
public class DetailedWeatherController {
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private MessageService messageService;


    private List<HourlyWeatherDTO> hourlyWeatherList;
    private List<DailyWeatherDTO> dailyWeatherList;
    private String locationQuery;
    private static final Logger logger = LoggerFactory.getLogger(DetailedWeatherController.class);


    /**
     * Initializes the weather details by retrieving data based on the provided location parameter.
     * If the location parameter is null, displays a warning message.
     * Logs information about the initialization process.
     */
    @PostConstruct
    public void init() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            locationQuery = externalContext.getRequestParameterMap().get("location");

            if (locationQuery != null) {
                retrieveWeatherData(locationQuery);
            } else {
                String message = "An error occurred!";
                messageService.showWarnMessage(message);
                logger.info("init did not work");
            }
        }
        catch (Exception e) {
            System.out.println("error init"+ e.getMessage());
            String message = "An error occurred!";
            messageService.showWarnMessage(message);
        }
    }

    
    /**
     * Retrieves weather data for the given location query.
     * Handles exceptions and displays a warning message if an error occurs.
     *
     * @param locationQuery The location query for which weather data is fetched.
     */
    private void retrieveWeatherData(String locationQuery) {
        // should not throw any exception, as it is tested before if the location query is valid
        try {
            Location location = locationService.retrieveLocation(locationQuery);
            CurrentlyHourlyDailyWeather weatherData =  weatherService.processWeatherForLocation(location);

            setHourlyWeatherList(weatherData.getHourlyWeatherList());
            setDailyWeatherList(weatherData.getDailyWeatherList());
            System.out.println("reach");


        } catch (EmptyLocationException | ApiQueryException e) {
            String message = "An error occurred!";
            messageService.showWarnMessage(message);
            System.out.println("error");
            logger.error("An error occurred in detailed controller: {}", e.getMessage(), e);
        }
    }

    public List<HourlyWeatherDTO> getHourlyWeatherList() {
        return hourlyWeatherList;
    }

    public void setHourlyWeatherList(List<HourlyWeatherDTO> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }

    public List<DailyWeatherDTO> getDailyWeatherList() {
        return dailyWeatherList;
    }

    public void setDailyWeatherList(List<DailyWeatherDTO> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    public String getLocationQuery() {
        return locationQuery;
    }

    public void setLocationQuery(String locationQuery) {
        this.locationQuery = locationQuery;
    }
}
