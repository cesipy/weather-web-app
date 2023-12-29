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

@Controller
@Scope("view")
public class LocationControllerDb {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationControllerDb.class);
    @Autowired
    private LocationService locationService;
    private String locationName;
    private Location currentLocation;

    public List<Location> autocomplete(String query)  {
        try {
            if (Objects.equals(locationName, "")) {
                LOGGER.info("Query is null!");
            }

            List <Location> locations = locationService.autocomplete(query);
            if (!locations.isEmpty()) {
                LOGGER.info("Autocomplete successful");
            }

            for (Location location : locations) {
                LOGGER.info(location.toDebugString());
            }

            return locations;
        }
        catch (EmptyLocationException e) {
            LOGGER.info("empty location list in autocomplete!");
        }

        return Collections.emptyList();
    }

    public Location requestFirstMatch() {
        try {
            // as autocompletion saves locationName to the form  <locationName, abbreviatedCountry>
            // only locationName has to be extracted
            String extractedLocationName = locationName.split(",")[0];

            List<Location> locations = locationService.autocomplete(extractedLocationName);

            if (!locations.isEmpty()) {
                Location firstLocation = locations.get(0);
                currentLocation = firstLocation;
                LOGGER.info(firstLocation.toDebugString());
                return firstLocation;
            } else {
                LOGGER.info("location is empty!");
            }
        }
        catch (EmptyLocationException e) {
            LOGGER.info("no location found");
            currentLocation = null;
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
    public String currentLocationToString() {
        if (currentLocation == null) {
            return "";
        }
        return currentLocation.toDebugString();
    }
}
