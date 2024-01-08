package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.internal.model.Favorite;
import at.qe.skeleton.internal.services.FavoriteService;
import at.qe.skeleton.internal.services.UserxService;
import jakarta.annotation.PostConstruct;
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

    ResponseEntity<List<Favorite>> responseEntity;



    public void saveFavorite() {
        favoriteService.saveFavorite(locationName, priority);
        LOGGER.info("successfully saved favorite: " + locationName + ", priority: " +priority);
    }

    public ResponseEntity<List<Favorite>> retrieveFavorites() {
        // get favorites for the user that
        List<Favorite> favorites = favoriteService.getFavoritesForUser();

        // TODO: handle user not found
        if (favorites.isEmpty()) {
            responseEntity = ResponseEntity.notFound().build();
            LOGGER.info("favorites is null!");
            return ResponseEntity.notFound().build();
        }

        for (Favorite favorite: favorites) {
            LOGGER.info(String.valueOf(favorite));
        }

        ResponseEntity<List<Favorite>> response = ResponseEntity.ok(favorites);
        responseEntity = response;
        return response;
    }


    public ResponseEntity<List<Favorite>> getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity<List<Favorite>> responseEntity) {
        this.responseEntity = responseEntity;
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
