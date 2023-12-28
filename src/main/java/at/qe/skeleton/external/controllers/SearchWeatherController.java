package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.location.LocationDTO;
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
    private LocationControllerApi locationController;
    private CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO;
    // all values are initialized as null
    private String currentWeather;
    private LocationDTO currentLocationDTO;
    private String currentLocation;
    private String locationToSearch;

    public void searchWeatherByLocation() {
        locationController.setLocationName(locationToSearch);

        locationController.search();
        setCurrentLocationDTO(locationController.getCurrentLocationDTO());
        setCurrentLocation(locationController.getCurrentLocation());

        weatherController.setLatitude(currentLocationDTO.latitude());
        weatherController.setLongitude(currentLocationDTO.longitude());
        weatherController.requestWeather();
        setCurrentAndForecastAnswerDTO(weatherController.getCurrentWeatherDTO());
        setCurrentWeather(weatherController.getCurrentWeather());

        // LOGGER.info("weather string in searchWeatherController: " + currentWeather);
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

    public LocationDTO getCurrentLocationDTO() {
        return currentLocationDTO;
    }

    public void setCurrentLocationDTO(LocationDTO currentlLocationDTO) {
        this.currentLocationDTO = currentlLocationDTO;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getLocationToSearch() {
        return locationToSearch;
    }

    public void setLocationToSearch(String locationToSearch) {
        this.locationToSearch = locationToSearch;
    }
}
