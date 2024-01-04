package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.Favorite;
import at.qe.skeleton.internal.services.FavoriteService;
import at.qe.skeleton.internal.services.UserxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FavoriteController {

    private static Logger LOGGER = LoggerFactory.getLogger(FavoriteController.class);

    private String searchUser;

    private String tempo = "Hallo from favorite controller!";

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
            private UserxService userxService;

    ResponseEntity<List<Favorite>> responseEntity;


    public ResponseEntity<List<Favorite>> retrieveFavorites(String username) {
        LOGGER.info("successfully entered retrieve Function!");
        List<Favorite> favorites = favoriteService.getFavoritesByUsername(username);
        LOGGER.info("fetched favorites using favoriteService!");

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
