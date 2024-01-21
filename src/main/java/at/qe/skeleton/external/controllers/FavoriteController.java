package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.FavoriteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class handling user interactions for managing favorite locations.
 */
@Controller
@Scope("view")
public class FavoriteController {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);
    private String locationName;
    private int priority;
    @Autowired
    private FavoriteService favoriteService;
    private List<Favorite> favorites;
    private List<Location> locations;
    private int updatedPriority;
    private Favorite selectedFavorite;

    @PostConstruct
    public void init() {
        locations = new ArrayList<>();
        // temporary for debugging
        locationName = "Vienna";
        saveFavorite();
        locationName = "Absam";
        saveFavorite();
    }


    /**
     * Moves a favorite location up in priority.
     *
     * @param favorite The favorite location to be moved.
     */
    public void moveFavoriteUp(Favorite favorite) {
        boolean up = true;
        favoriteService.moveFavoriteUpOrDown(favorite, up);
    }


    /**
     * Moves a favorite location down in priority.
     *
     * @param favorite The favorite location to be moved.
     */
    public void moveFavoriteDown(Favorite favorite) {
        boolean up = false;
        favoriteService.moveFavoriteUpOrDown(favorite, up);
    }


    /**
     * Saves a favorite location if it is not already in the list of favorites.
     * Displays an error message if the location is already a favorite.
     */
    public void saveFavorite() {
        try {
            validateAndSaveFavorite();
        }
        catch (EmptyLocationException e) {
            String warnMessage = "Cannot find city: %s".formatted(locationName);
            showInfoMessage(warnMessage);
        }
        catch (ApiQueryException e) {
            String warnMessage = "Error occurred while fetching weather data";
            showWarnMessage(warnMessage);
        }

        catch (Exception e) {
            showWarnMessage("An error occurred.");
            logger.error("Error saving favorite", e);
        }
    }


    /**
     * Validates the input location name and saves it as a favorite if it meets the criteria.
     * If the input location is not valid, a message is shown
     *
     * @throws EmptyLocationException if the location name is null or empty after trimming.
     */
    private void validateAndSaveFavorite() throws EmptyLocationException, ApiQueryException {

        if (locationName == null || locationName.trim().isEmpty()) {
            String warnMessage = "Please enter a city.";
            showWarnMessage(warnMessage);
            return;
        }

        if (favoriteService.isLocationAlreadyFavorite(locationName)) {
            String warnMessage = "Location already in favorites: %s".formatted(locationName);
            showWarnMessage(warnMessage);
        } else {
            favoriteService.saveFavorite(locationName);

            // clear locationName after save
            locationName = "";
        }
    }


    /**
     * Retrieves the list of favorite locations for the current user.
     */
    public void retrieveFavorites() {
        try {
            // Get favorites for the user
            favorites = favoriteService.getSortedFavoritesForUser();

            for (Favorite favorite : favorites) {

                // load all locations
                locations.add(favorite.getLocation());
            }

        } catch (Exception e) {
            logger.error("Error retrieving favorites", e);
        }
    }


    /**
     * Deletes a favorite location and updates the list of favorites.
     *
     * @param favorite The favorite location to be deleted.
     */
    public void deleteFavorite(Favorite favorite) {
        try {
            favoriteService.deleteFavorite(favorite);
            logger.info("Successfully deleted favorite: {}" , favorite.getLocation().getName());

            // update favorite list
            retrieveFavorites();
        } catch (Exception e) {
            logger.error("Error deleting favorite", e);
        }
    }


    /**
     * Deletes a favorite location by its ID and updates the list of favorites.
     *
     * @param id The ID of the favorite location to be deleted.
     */
    public void deleteFavoriteById(Long id) {
        //currently not in use
        try {
            favoriteService.deleteFavoriteById(id);

            // update favorite list
            retrieveFavorites();
        } catch (Exception e) {
            logger.error("Error deleting favorite", e);
        }
    }


    /**
     * Retrieves a list of locations matching the given query for autocomplete suggestions.
     *
     * @param query The input query for location autocomplete.
     * @return A list of matching locations.
     */
    public List<Location> autocomplete(String query) {
        try {

            return favoriteService.autocomplete(query);
        } catch (EmptyLocationException e) {
            logger.info("exception in autocomplete!");
        }
        return null; // only temp
    }


    /**
     * Displays a warning message in the user interface.
     *
     * @param message The warning message to be shown.
     */
    public void showWarnMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Warning:",
                        message));

    }


    /**
     * Displays an informative message in the user interface.
     *
     * @param message The warning message to be shown.
     */
    public void showInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info:", message));
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getUpdatedPriority() {
        return updatedPriority;
    }

    public void setUpdatedPriority(int updatedPriority) {
        this.updatedPriority = updatedPriority;
    }

    public Favorite getSelectedFavorite() {
        return selectedFavorite;
    }

    public void setSelectedFavorite(Favorite selectedFavorite) {
        this.selectedFavorite = selectedFavorite;
    }
}
