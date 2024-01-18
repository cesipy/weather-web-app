package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for managing locations retrieved from the database.
 */
@Controller
@Scope("view")
public class LocationControllerDb {
    private static final Logger logger = LoggerFactory.getLogger(LocationControllerDb.class);
    @Autowired
    private LocationService locationService;
    private String locationName;
    private Location currentLocation;


    /**
     * Provides a list of locations based on the given query for autocomplete functionality.
     *
     * @param query The query for autocompletion.
     * @return A list of locations matching the provided query.
     */
    public List<Location> autocomplete(String query)  {
        try {


            if (Objects.equals(locationName, "")) {
                logger.info("Query is null!");
            }

            List<Location> locations = locationService.autocomplete(query);

            if (locations.isEmpty()) {
                logger.info("empty location list in autocomplete!");
                return Collections.emptyList();
            }

            logLocations(locations);
            return locations;
        } catch (EmptyLocationException e) {
            logger.info("in locationcontrollerDB: {} ", e.getMessage());
        }

        return null; // only temp
    }


    /**
     * Retrieves the first matching location based on the provided query.
     * This is used to search a specific location from the database.
     *
     * @return The first matching location.
     */
    public Location requestFirstMatch() {
        try {
            // as autocompletion saves locationName to the form  <locationName, abbreviatedCountry>
            // only locationName has to be extracted
            String extractedLocationName = locationName.split(",")[0];

            List<Location> locations = locationService.autocomplete(extractedLocationName);

            if (locations.isEmpty()) {
                logger.info("no location found");
                currentLocation = null;
                return null;
            }

            Location firstLocation = locations.get(0);
            currentLocation = firstLocation;

            return firstLocation;
        } catch (EmptyLocationException e) {
            logger.info("no location found! {}", e.getMessage());
        }
        return null;
    }

    //TODO: convert LocationEntity to .json string

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    // only temporary
    /**
     * Provides a string representation of the current location for temporary use.
     *
     * @return A string representation of the current location.
     */
    public String currentLocationToString() {
        if (currentLocation == null) {
            return "";
        }
        return currentLocation.toDebugString();
    }

    /**
     * Logs the provided list of locations.
     *
     * @param locations The list of locations to be logged.
     */
    private void logLocations(List<Location> locations) {
        for (Location location : locations) {
            logger.info(location.toDebugString());
        }
    }

}
