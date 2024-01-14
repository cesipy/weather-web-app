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

    // threshold to retrieve previously saved weather data, in seconds
    private static final long STALE_WEATHER_DATA_THRESHOLD_SECONDS = 600;



    @PostConstruct
    public void init() {
        favorites = new ArrayList<>();
        selectedFieldList = new ArrayList<>();
        currentWeatherDataList = new ArrayList<>();

        // needed to refresh favorites
        retrieveFavorites();
    }

    /**
     * Retrieves user favorites and updates the current weather data list for display.
     */
    public void retrieveFavorites() {
        // clear the existing list before retrieving new favorites
        currentWeatherDataList.clear();
        favorites = favoriteService.getSortedFavoritesForUser();
        selectedFieldList = favoriteService.retrieveSelectedFields();

        if (favorites.isEmpty()) {
            LOGGER.info("favorites in overview are empty");
        }

        fetchWeatherDataForFavorites();
    }

    /**
     * Fetches the current weather data for each favorite location and adds it to the list.
     * If the weather data is not present in the local database, an API request is made
     * to retrieve the current weather information.
     *
     * @see #fetchCurrentWeather(Location)
     */
    private void fetchWeatherDataForFavorites() {
        for (Favorite favorite : favorites) {
            Location location = favorite.getLocation();
            CurrentWeatherData currentWeatherData = fetchCurrentWeather(location);
            currentWeatherDataList.add(currentWeatherData);
        }
    }

    /**
     * Fetches current weather data for a given location, either from the database or by making an API request.
     *
     * @param location The location for which to fetch weather data.
     * @return The current weather data for the specified location.
     */
    private CurrentWeatherData fetchCurrentWeather(Location location) {
        List<CurrentWeatherData> currentWeatherDataList = currentWeatherDataRepository
                .findByLocationOrderByAdditionTimeDesc(location);

        // is data outdated or no data is saved?
        if (currentWeatherDataList.isEmpty() || isWeatherDataStale(currentWeatherDataList.get(0))) {
            CurrentAndForecastAnswerDTO weather = weatherApiRequestService
                    .retrieveCurrentAndForecastWeather(location.getLatitude(), location.getLongitude());

            weatherDataService.saveCurrentWeatherFromDTO(weather.currentWeather(), location);

            return currentWeatherDataRepository
                    .findByLocationOrderByAdditionTimeDesc(location).get(0);
        } else {
            LOGGER.info("Taking weather data from database for location {}", location);
            return currentWeatherDataList.get(0);
        }
    }

    /**
     * Checks if the provided weather data is considered stale based on the time elapsed since its addition.
     *
     * @param weatherData The weatherData to check for staleness.
     * @return {@code true} if the weather data is considered stale, {@code false} otherwise.
     */
    private boolean isWeatherDataStale(CurrentWeatherData weatherData) {
        Instant additionTime = weatherData.getAdditionTime();
        Instant nowTime = Instant.now();
        Duration timeElapsed = Duration.between(additionTime, nowTime);
        return timeElapsed.getSeconds() >= STALE_WEATHER_DATA_THRESHOLD_SECONDS;
    }


    /**
     * Checks if a field is selected by the user
     * It does so by checking if a given field name is present in the list of selected weather data fields.
     *
     * @param fieldName The name of the field to check.
     * @return true if the field is in the list, false otherwise.
     */
    public boolean isInList(String fieldName) {
        WeatherDataField[] selectedFields = WeatherDataField.values();
        for (WeatherDataField field : selectedFieldList) {
            if (field.name().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the event triggered when a column is toggled in the DataTable in Primefaces.
     *
     * @param e The ToggleEvent containing information about the toggle event.
     */
    public void onToggle(ToggleEvent e) {
        int columnIndex = (int) e.getData();
        Visibility visibility = e.getVisibility();

        // hard coded WeatherDataFields. AJAX toggle only returns an integer that has to be mapped to the
        // corresponding WeatherDataField.
        // there are simpler ways, but they are not really readable.
        switch (columnIndex) {
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
            default:
                LOGGER.warn("Unexpected value in onToggle: " + columnIndex);
        }
    }

    /**
     * Updates the list of selected weather fields based on the visibility of a specific field.
     *
     * @param weatherDataField The weather data field to update.
     * @param visibility The visibility status of the field.
     */
    private void updateSelectedField(WeatherDataField weatherDataField, Visibility visibility) {
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

    public List<CurrentWeatherData> getCurrentWeatherDataList() {
        return currentWeatherDataList;
    }

    public void setCurrentWeatherDataList(List<CurrentWeatherData> currentWeatherDataList) {
        this.currentWeatherDataList = currentWeatherDataList;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }
}
