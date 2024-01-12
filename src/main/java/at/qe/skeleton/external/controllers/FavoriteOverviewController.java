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
        favoriteService.setDefaultSelectedFields();

        // needed to refresh favorites
        retrieveFavorites();
    }

    public void retrieveFavorites() {
        // clear the existing list before retrieving new favorites
        currentWeatherDataList.clear();

        favorites = favoriteService.getSortedFavoritesForUser();

        selectedFieldList = favoriteService.retrieveSelectedFields();

        if (favorites.isEmpty()) {
            LOGGER.info("favorites in overview are empty");
        }

        for (Favorite favorite : favorites) {
            Location location = favorite.getLocation();
            CurrentWeatherData currentWeatherData = fetchCurrentWeather(location);

            currentWeatherDataList.add(currentWeatherData);
        }
    }

    public CurrentWeatherData fetchCurrentWeather(Location location) {
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
        LOGGER.info(String.valueOf(selectedFieldList));
        WeatherDataField[] selectedFields = WeatherDataField.values();
        LOGGER.info(fieldName);
        for (WeatherDataField field : selectedFieldList) {
            if (field.name().equals(fieldName)) {
                LOGGER.info( field.name() + "is in list" + ", " + fieldName);
                return true;
            }
        }
        LOGGER.info("Not in list");
        return false;
    }

    public List<CurrentWeatherData> getCurrentWeatherDataList() {
        return currentWeatherDataList;
    }

    public void setCurrentWeatherDataList(List<CurrentWeatherData> currentWeatherDataList) {
        this.currentWeatherDataList = currentWeatherDataList;
    }
}
