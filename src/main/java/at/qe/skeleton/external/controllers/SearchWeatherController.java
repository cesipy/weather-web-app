package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.location.Location;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller             // @Controller is a specification of @Component
@Scope("view")
public class SearchWeatherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchWeatherController.class);
    @Autowired
    private WeatherController weatherController;
    @Autowired
    private LocationControllerDb locationControllerDb;
    private CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO;
    private String currentWeather;
    private Location currentLocation;
    private String currentLocationString;
    private String locationToSearch;

    /**
     * Starts a weather search based on the specified location.
     * The location is set in the {@link LocationControllerDb} to facilitate autocompletion.
     * The search is performed using the database, and if a location is found, weather information is retrieved
     * and stored in the controller's fields.
     * If no location is found, the current weather is set to "No location found."
     */
    public void searchWeatherByLocation() {
        locationControllerDb.setLocationName(locationToSearch);

        // currently all locations are processed by database and not the geocoding api. this makes autocompletion much
        // easier. however, there are cities missing. example:
        // query in database= "inns" result: only innsbruck
        // query in api    = "inns" result: Inns quay B, Inns quay A, Inns quay C, Inns
        Location singleLocation = locationControllerDb.requestFirstMatch();

        if (singleLocation != null) {
            processWeatherForLocation(singleLocation);
        }
        else {
            // shows a faceMessage when no location is found
            showInfoMessage(locationToSearch);
            LOGGER.info("in searchWeatherByLocation: no location found");
        }
    }

    /**
     * Processes weather information for a given location.
     * The location details are set in the controller, and the weather is retrieved using the {@link WeatherController}.
     *
     * @param singleLocation location for which weather information is to be retrieved
     */
    private void processWeatherForLocation(Location singleLocation) {
        setCurrentLocation(singleLocation);
        setCurrentLocationString(singleLocation.toDebugString());

        weatherController.setLatitude(singleLocation.getLatitude());
        weatherController.setLongitude(singleLocation.getLongitude());

        weatherController.requestWeather();
        setCurrentAndForecastAnswerDTO(weatherController.getCurrentWeatherDTO());
        setCurrentWeather(weatherController.getCurrentWeather());
    }

    /**
     * Displays an informational message about a location not being found.
     *
     * @param locationName The name of the location for which the message is generated.
     */
    public void showInfoMessage(String locationName) {
        String message = String.format("Location '%s' not found!", locationName);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info:", message));
    }

    public CurrentAndForecastAnswerDTO getCurrentAndForecastAnswerDTO() {
        return currentAndForecastAnswerDTO;
    }

    public void setCurrentAndForecastAnswerDTO(CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO) {
        this.currentAndForecastAnswerDTO = currentAndForecastAnswerDTO;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String currentWeatherString) {
        this.currentWeather = currentWeatherString;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getCurrentLocationString() {
        return (currentLocationString == null) ? "no location found" : currentLocationString;
    }

    public void setCurrentLocationString(String currentLocationString) {
        this.currentLocationString = currentLocationString;
    }

    public String getLocationToSearch() {
        return locationToSearch;
    }

    public void setLocationToSearch(String locationToSearch) {
        this.locationToSearch = locationToSearch;
    }
}
