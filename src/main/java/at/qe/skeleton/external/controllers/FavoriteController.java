package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.Favorite;
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
    private static Logger logger = LoggerFactory.getLogger(FavoriteController.class);
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
            boolean isAlreadyFavorite = favoriteService.isLocationAlreadyFavorite(locationName);

            if (isAlreadyFavorite) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "location already in favorites!",
                                        null));
                logger.info("location {} is already favorite!", locationName);
            }
            else {
                favoriteService.saveFavorite(locationName);
            }
            // clear locationName after save
            locationName = "";

        } catch (Exception e) {

            logger.error("Error saving favorite", e);
        }
    }

    /**
     * Retrieves the list of favorite locations for the current user.
     */
    public void retrieveFavorites() {
        try {
            // Get favorites for the user
            favorites = favoriteService.getSortedFavoritesForUser();

            // Log favorites
            for (Favorite favorite : favorites) {
                logger.info(String.valueOf(favorite));

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
            logger.info("Successfully deleted favorite with id: " + id);

            // update favorite list
            retrieveFavorites();
        } catch (Exception e) {
            logger.error("Error deleting favorite", e);
        }
    }

    /**
     * Updates the priority of a favorite location.
     *
     * @param favorite The favorite location whose priority is to be updated.
     * @param priority The new priority value.
     */
    public void updateFavoritePriority(Favorite favorite, int priority) {
        // currently not in use, might be needed later
        if (favorite != null) {

            favoriteService.updateFavoritePriority(favorite, priority);

            // clear selectedFavorite to avoid unintentional updates
            selectedFavorite = null;
            updatedPriority = 0;

            logger.info("Successfully updated priority for favorite with ID: {} ", favorite.getId());
            retrieveFavorites();
        }
    }

    public List<Location> autocomplete(String query) {
        return favoriteService.autocomplete(query);
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
