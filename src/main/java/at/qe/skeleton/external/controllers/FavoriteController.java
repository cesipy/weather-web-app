package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.MessageService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller class handling user interactions for managing favorite locations.
 */
@Controller
@Scope("view")
public class FavoriteController {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);
    private String locationName;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private MessageService messageService;
    private List<Favorite> favorites;
    private List<Location> locations;


    @PostConstruct
    public void init() {
        locations = new ArrayList<>();
        favorites = new ArrayList<>();
    }


    /**
     * Moves a favorite location up in priority.
     *
     * @param favorite The favorite location to be moved.
     */
    public void moveFavoriteUp(Favorite favorite) {
        boolean up = true;
        favoriteService.moveFavoriteUpOrDown(favorite, up);

        // update favorites
        // necessary to immediately update favorites in manageFavorites
        retrieveFavorites();
    }


    /**
     * Moves a favorite location down in priority.
     *
     * @param favorite The favorite location to be moved.
     */
    public void moveFavoriteDown(Favorite favorite) {
        boolean up = false;
        favoriteService.moveFavoriteUpOrDown(favorite, up);

        // update favorites
        // necessary to immediately update favorites in manageFavorites
        retrieveFavorites();
    }


    /**
     * Saves a favorite location if it is not already in the list of favorites.
     * Displays an error message if the location is already a favorite.
     */
    public void saveFavorite() {
        try {
            validateAndSaveFavorite();

            // update favorites
            // necessary to immediately update favorites in manageFavorites
            retrieveFavorites();
        }
        catch (EmptyLocationException e) {
            String warnMessage = "Cannot find city: %s".formatted(locationName);
            messageService.showInfoMessage(warnMessage);
            logger.error("Error saving favorite, location is not known", e);
        }
        catch (ApiQueryException e) {
            String warnMessage = "Error occurred while fetching weather data";
            messageService.showWarnMessage(warnMessage);
            logger.error("Error occurred while saving favorite, API didn't work", e);
        }

        catch (Exception e) {
            String message = "An error occurred!";
            messageService.showWarnMessage(message);
            logger.error("Error occurred saving favorite", e);
        }
    }


    /**
     * Validates the input location name and saves it as a favorite if it meets the criteria.
     * If the input location is not valid, a message is shown
     *
     * @throws EmptyLocationException if the location name is null or empty after trimming.
     */
    public void validateAndSaveFavorite() throws EmptyLocationException, ApiQueryException {
        try {
            if (locationName == null || locationName.trim().isEmpty()) {
                String warnMessage = "Please enter a city.";
                messageService.showWarnMessage(warnMessage);
                return;
            }

            if (favoriteService.isLocationAlreadyFavorite(locationName)) {
                String warnMessage = "Location already in favorites: %s".formatted(locationName);
                messageService.showWarnMessage(warnMessage);
            } else {
                favoriteService.saveFavorite(locationName);
                // clear locationName after save
                locationName = "";
            }
        } catch (EmptyLocationException | ApiQueryException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error saving favorite", e);
            throw new EmptyLocationException("Error saving favorite");
        }
    }


    /**
     * Retrieves the list of favorite locations for the current user.
     */
    public void retrieveFavorites() {
        try {

            if (locations == null) {
                locations = new ArrayList<>();
            }
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

        favoriteService.deleteFavorite(favorite);

        // update favorite list
        retrieveFavorites();

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
            logger.error("An error occurred in autocomplete!");
        }
        return Collections.emptyList();
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

    public List<Location> getLocations() {
        return locations;
    }

}
