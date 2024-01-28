package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.WeatherDataField;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.external.services.*;
import at.qe.skeleton.external.model.Favorite;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("view")
public class FavoriteOverviewController {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteOverviewController.class);
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    @Autowired
    private WeatherDataService weatherDataService;
    @Autowired
    private CurrentWeatherDataRepository currentWeatherDataRepository;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private MessageService messageService;

    private List<Favorite> favorites;
    private List<CurrentWeatherData> currentWeatherDataList;
    private List<WeatherDataField> selectedFieldList;

    // threshold to retrieve previously saved weather data, in seconds


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
        try {
            // clear the existing list before retrieving new favorites
            currentWeatherDataList.clear();
            favorites = favoriteService.getSortedFavoritesForUser();
            selectedFieldList = favoriteService.retrieveSelectedFields();

            if (favorites.isEmpty()) {
                logger.info("favorites in overview are empty");
            }

            fetchWeatherDataForFavorites();
        }
        catch (ApiQueryException e) {
            logger.info("Error occurred while requesting API");
            String message = "Could not fetch weather data!";
            messageService.showWarnMessage(message);
        }
        catch (Exception e) {
            logger.info("error occurred!");
        }
    }

    /**
     * Fetches the current weather data for each favorite location and adds it to the list.
     * If the weather data is not present in the local database, an API request is made
     * to retrieve the current weather information.
     *
     */
    private void fetchWeatherDataForFavorites() throws ApiQueryException {
        for (Favorite favorite : favorites) {
            Location location = favorite.getLocation();
            CurrentWeatherData currentWeatherData = weatherService.fetchCurrentWeather(location);
            currentWeatherDataList.add(currentWeatherData);
        }
    }

    /**
     * Checks if a field is selected by the user
     * It does so by checking if a given field name is present in the list of selected weather data fields.
     *
     * @param fieldName The name of the field to check.
     * @return true if the field is in the list, false otherwise.
     */
    public boolean isInList(String fieldName) {
        for (WeatherDataField field : selectedFieldList) {
            if (field.name().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Redirects to the detail page for the specified location.
     * Retrieves the location ID from the request parameters, fetches the location details,
     * and redirects to the detail page.
     */
    public void onViewDetails() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String locationId = facesContext.getExternalContext().getRequestParameterMap().get("locationName");

            if (locationId != null) {
                Location location = locationService.retrieveLocation(locationId);

                if (location != null) {
                    redirectToDetailPage(location);
                } else {
                    // Handle the case where location details are not found
                    String message = "Location details not found.";
                    messageService.showWarnMessage(message);
                }
            }
        }
        // should not happen, as only valid locations are saved to favorites
        catch (Exception e) {
            logger.info("error occurred: {}", e.getMessage());
        }
    }

    /**
     * Redirects to the detail page for the specified location.
     *
     * @param location The location for which to redirect to the detail page.
     */
    private void redirectToDetailPage(Location location) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String url = externalContext.getRequestContextPath() + "/common/detail.xhtml?location=" + location.getName();

        try {
            externalContext.redirect(url);
        } catch (IOException e) {
            messageService.showWarnMessage("An error occurred!");
        }
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
                logger.info("Column index not found!");
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
            favoriteService.addSelectedFields(List.of(weatherDataField));
        }
        else if (visibility == Visibility.HIDDEN) {
            favoriteService.deleteSelectedFields(List.of(weatherDataField));
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

    public List<WeatherDataField> getSelectedFieldList() {
        return selectedFieldList;
    }

    public void setSelectedFieldList(List<WeatherDataField> selectedFieldList) {
        this.selectedFieldList = selectedFieldList;
    }
}
