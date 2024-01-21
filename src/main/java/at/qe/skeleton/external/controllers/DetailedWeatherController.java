package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.services.WeatherService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
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
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        locationQuery = externalContext.getRequestParameterMap().get("location");

        if (locationQuery != null) {
            retrieveWeatherData(locationQuery);
        }
        else {
            displayWarningMessage();
            logger.info("init did not work");
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

            hourlyWeatherList = weatherData.getHourlyWeatherList();
            dailyWeatherList  = weatherData.getDailyWeatherList();


        } catch (EmptyLocationException | ApiQueryException e) {
            displayWarningMessage();
            logger.error("An error occurred in detailed controller: {}", e.getMessage(), e);
        }
    }


    /**
     * Displays a warning message on the user interface.
     */
    public void displayWarningMessage() {
        String message = "An error occurred!";
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning:", message));
    }


    /**
     * Displays an informational message about a location not being found.
     *
     * @param locationName The name of the location for which the message is generated.
     */
    public void displayInfoMessage(String locationName) {
        String message = String.format("Location '%s' not found!", locationName);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info:", message));
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
