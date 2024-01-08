package at.qe.skeleton.internal.ui.controllers;

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

    private String searchUser;

    private String tempo = "Hallo from favorite controller!";

    @Autowired
    private FavoriteService favoriteService;

    ResponseEntity<List<Favorite>> responseEntity;


    // temporary for demo, adds favorites to admin
    @PostConstruct
    public void init() {
        favoriteService.saveFavorite("Absam", 1);
        favoriteService.saveFavorite("Innsbruck", 2);
        LOGGER.info("saved favorites for demo");
    }

    public ResponseEntity<List<Favorite>> retrieveFavorites(String username) {
        List<Favorite> favorites = favoriteService.getFavoritesByUsername(username);

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
        LOGGER.info("retrieval successful");
        return response;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public ResponseEntity<List<Favorite>> getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity<List<Favorite>> responseEntity) {
        this.responseEntity = responseEntity;
    }

    public String getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(String searchUser) {
        this.searchUser = searchUser;
    }
}
