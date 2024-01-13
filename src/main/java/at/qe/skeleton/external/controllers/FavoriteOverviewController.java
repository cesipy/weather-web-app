package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.WeatherDataField;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.services.WeatherDataService;
import jakarta.annotation.PostConstruct;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("view")
public class FavoriteOverviewController {
    private static Logger LOGGER = LoggerFactory.getLogger(FavoriteOverviewController.class);
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    @Autowired
    private WeatherDataService weatherDataService;
    @Autowired
    private CurrentWeatherDataRepository currentWeatherDataRepository;
    private List<Favorite> favorites;
    private List<CurrentWeatherData> currentWeatherDataList;
    private List<WeatherDataField> selectedFieldList;


    @PostConstruct
    public void init() {
        favorites = new ArrayList<>();
        selectedFieldList = new ArrayList<>();
        currentWeatherDataList = new ArrayList<>();

        // needed to refresh favorites
        retrieveFavorites();
    }

    public void retrieveFavorites() {
        // clear the existing list before retrieving new favorites
        currentWeatherDataList.clear();

        favorites = favoriteService.getSortedFavoritesForUser();
        selectedFieldList = favoriteService.retrieveSelectedFields();
        LOGGER.info("selected fields: " + selectedFieldList);

        if (favorites.isEmpty()) {
            LOGGER.info("favorites in overview are empty");
        }

        for (Favorite favorite : favorites) {
            Location location = favorite.getLocation();
            CurrentWeatherData currentWeatherData = fetchCurrentWeather(location);

            currentWeatherDataList.add(currentWeatherData);
        }
    }

    private CurrentWeatherData fetchCurrentWeather(Location location) {
        List<CurrentWeatherData> currentWeatherDataList =  currentWeatherDataRepository
                .findByLocationOrderByAdditionTimeDesc(location);

        if (currentWeatherDataList.isEmpty()) {
            CurrentAndForecastAnswerDTO weather = weatherApiRequestService
                    .retrieveCurrentAndForecastWeather(location.getLatitude(), location.getLongitude());

            weatherDataService.saveCurrentWeatherFromDTO(weather.currentWeather(), location);

            return currentWeatherDataRepository
                    .findByLocationOrderByAdditionTimeDesc(location).get(0);
        }
        Instant additionTime = currentWeatherDataList.get(0).getAdditionTime();
        Instant nowTime = Instant.now();
        Duration timeElapsed = Duration.between(additionTime, nowTime);

        if (timeElapsed.getSeconds() < 600) {
            LOGGER.info("Taking weather data from database!");
            return currentWeatherDataList.get(0);
        } else {
            CurrentAndForecastAnswerDTO weather = weatherApiRequestService
                    .retrieveCurrentAndForecastWeather(location.getLatitude(), location.getLongitude());

            weatherDataService.saveCurrentWeatherFromDTO(weather.currentWeather(), location);

            LOGGER.info(" weather data is too old, new fetch!");
            return currentWeatherDataRepository
                    .findByLocationOrderByAdditionTimeDesc(location).get(0);
        }
    }

    public boolean isInList(String fieldName) {
        WeatherDataField[] selectedFields = WeatherDataField.values();
        for (WeatherDataField field : selectedFieldList) {
            if (field.name().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public List<CurrentWeatherData> getCurrentWeatherDataList() {
        return currentWeatherDataList;
    }

    public void setCurrentWeatherDataList(List<CurrentWeatherData> currentWeatherDataList) {
        this.currentWeatherDataList = currentWeatherDataList;
    }

    public void onToggle(ToggleEvent e) {
        int data = (int) e.getData();
        Visibility visibility = e.getVisibility();

        LOGGER.info("in onToggle with event: " + e + ", " + data);
        LOGGER.info("visibility for event: " + visibility);

        // hard coded WeatherDataFields. AJAX toggle only returns an integer that has to be mapped to the
        // corresponding WeatherDataField.
        // there are simpler ways, but they are not really readable.
        switch (data) {
            case 1:
                updateSelectedField(WeatherDataField.SUNRISE, visibility);
                break;
            case 2:
                updateSelectedField(WeatherDataField.SUNSET, visibility);
                break;
            case 3:
                updateSelectedField(WeatherDataField.TEMP, visibility);
                break;
            case 4:
                updateSelectedField(WeatherDataField.FEELS_LIKE, visibility);
                break;
            case 5:
                updateSelectedField(WeatherDataField.PRESSURE, visibility);
                break;
            case 6:
                updateSelectedField(WeatherDataField.HUMIDITY, visibility);
                break;
            case 7:
                updateSelectedField(WeatherDataField.DEW_POINT, visibility);
                break;
            case 8:
                updateSelectedField(WeatherDataField.CLOUDS, visibility);
                break;
            case 9:
                updateSelectedField(WeatherDataField.UVI, visibility);
                break;
            case 10:
                updateSelectedField(WeatherDataField.VISIBILITY, visibility);
                break;
            case 11:
                updateSelectedField(WeatherDataField.WIND_SPEED, visibility);
                break;
            case 12:
                updateSelectedField(WeatherDataField.WIND_DIRECTION, visibility);
                break;
            case 13:
                updateSelectedField(WeatherDataField.RAIN, visibility);
                break;
            case 14:
                updateSelectedField(WeatherDataField.SNOW, visibility);
                break;
            case 15:
                updateSelectedField(WeatherDataField.ICON, visibility);
                break;
            case 16:
                updateSelectedField(WeatherDataField.DESCRIPTION, visibility);
                break;
        }
    }

    public void updateSelectedField(WeatherDataField weatherDataField, Visibility visibility) {
        if (visibility == Visibility.VISIBLE) {
            LOGGER.info("set visibility for " + weatherDataField + " to visible" );
            favoriteService.addSelectedFields(List.of(weatherDataField));
        } else if (visibility == Visibility.HIDDEN) {
            LOGGER.info("set visibility for " + weatherDataField + " to hidden" );
            favoriteService.deleteSelectedFields(List.of(weatherDataField));
        }
        else {
            LOGGER.info("Error occurred!");
        }
    }
}
