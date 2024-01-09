package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.LocationAlreadyInFavoritesException;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("view")
public class FavoriteController {
    private static Logger LOGGER = LoggerFactory.getLogger(FavoriteController.class);
    private String locationName;
    private int priority;
    @Autowired
    private FavoriteService favoriteService;
    private List<Favorite> favorites;
    private List<Location> locations;
    private int updatedPriority;


    @PostConstruct
    public void init() {
        locations = new ArrayList<>();
    }

    public void updateFavoritePriority(Long id, int priority) {
        favoriteService.updateFavoritePriority(id, priority);
        LOGGER.info("Successfully updated priority for favorite with ID: " + id);

        // Update the favorite list
        retrieveFavorites();
    }

    public void saveFavorite() {
        try {
            boolean isAlreadyFavorite = favoriteService.isLocationAlreadyFavorite(locationName);

            if (isAlreadyFavorite) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "location already in favorites!",
                                        null));
                LOGGER.info("location " + locationName + " is already favorite!");
            }
            else {

                favoriteService.saveFavorite(locationName);
                LOGGER.info("Successfully saved favorite: " + locationName);

            }
            // clear locationName after save
            locationName = "";

        } catch (Exception e) {

            LOGGER.error("Error saving favorite", e);
        }
    }

    public void retrieveFavorites() {
        try {
            // Get favorites for the user
            favorites = favoriteService.getFavoritesForUser();

            // Log favorites
            for (Favorite favorite : favorites) {
                LOGGER.info(String.valueOf(favorite));

                // load all locations
                locations.add(favorite.getLocation());
            }

        } catch (Exception e) {
            LOGGER.error("Error retrieving favorites", e);
        }
    }

    public void deleteFavorite(Favorite favorite) {
        try {
            favoriteService.deleteFavorite(favorite);
            LOGGER.info("Successfully deleted favorite: " + favorite.getLocation().getName());

            // update favorite list
            retrieveFavorites();
        } catch (Exception e) {
            LOGGER.error("Error deleting favorite", e);
        }
    }

    public void deleteFavoriteById(Long id) {
        try {
            favoriteService.deleteFavoriteById(id);
            LOGGER.info("Successfully deleted favorite with id: " + id);

            // update favorite list
            retrieveFavorites();
        } catch (Exception e) {
            LOGGER.error("Error deleting favorite", e);
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
}
