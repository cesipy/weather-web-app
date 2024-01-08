package at.qe.skeleton.external.controllers;

import at.qe.skeleton.internal.model.Favorite;
import at.qe.skeleton.external.services.FavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

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

    public void saveFavorite() {
        favoriteService.saveFavorite(locationName, priority);
        LOGGER.info("successfully saved favorite: " + locationName + ", priority: " +priority);
    }

    public void retrieveFavorites() {
        // Get favorites for the user
        favorites = favoriteService.getFavoritesForUser();

        // Log favorites
        for (Favorite favorite : favorites) {
            LOGGER.info(String.valueOf(favorite));
        }
    }

    public void deleteFavorite(Favorite favorite) {
        favoriteService.deleteFavorite(favorite);
        LOGGER.info("Successfully deleted favorite: " + favorite.getLocation().getName());

        // update favorite list
        retrieveFavorites();
    }

    public void deleteFavoriteById(Long id) {
        favoriteService.deleteFavoriteById(id);
        LOGGER.info("Successfully deleted favorite with id: " + id);

        // update favorite list
        retrieveFavorites();

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
}
