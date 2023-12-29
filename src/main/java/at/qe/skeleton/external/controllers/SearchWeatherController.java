package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.location.Location;
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
    // all values are initialized as null
    private String currentWeather;
    private Location currentLocation;
    private String currentLocationString;
    private String locationToSearch;

    public void searchWeatherByLocation() {
        locationControllerDb.setLocationName(locationToSearch);

        // currently all locations are processed by database and not the geocoding api. this makes autocompletion much
        // easier. however, there are cities missing. example:
        // query in database= "inns" result: only innsbruck
        // query in appi    = "inns" result: Inns quay B, Inns quay A, Inns quay C, Inns

        //locationControllerDb.getFirstMatch();
        //Location singleLocation = locationControllerDb.getSingleLocation();
        Location singleLocation = locationControllerDb.requestFirstMatch();

        if (singleLocation != null) {

            setCurrentLocation(singleLocation);
            setCurrentLocationString(singleLocation.toDebugString());       // for debugging

            weatherController.setLatitude(singleLocation.getLatitude());
            weatherController.setLongitude(singleLocation.getLongitude());

            weatherController.requestWeather();
            setCurrentAndForecastAnswerDTO(weatherController.getCurrentWeatherDTO());
            setCurrentWeather(weatherController.getCurrentWeather());

            // LOGGER.info("weather string in searchWeatherController: " + currentWeather);
        }
        else {
            // TODO: improve error handling
            LOGGER.info("in searchWeatherByLocation: no location found");
            setCurrentWeather("No location found.");
        }
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
