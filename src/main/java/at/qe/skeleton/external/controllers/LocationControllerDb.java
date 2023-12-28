package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
@Scope("view")
public class LocationControllerDb {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationControllerDb.class);
    @Autowired
    private LocationService locationService;
    private String locationName;
    private List<Location> locations;
    private Location singleLocation;
    private double longitude;
    private double latitude;

    public List<Location> autocomplete(String query) {
        if (Objects.equals(locationName, "")) {
            LOGGER.info("Query is null!");
            return null;
        }

        locations = locationService.autocomplete(query);
        if (!locations.isEmpty()) {
            LOGGER.info("Autocomplete successful");
        }

        for (Location location : locations) {
            LOGGER.info(location.toDebugString());
        }

        return locations;
    }

    public void getFirstMatch() {
        LOGGER.info("location_name: " + locationName);

        // as autocompletion saves locationName to the form  <locationName, abbreviatedCountry>
        // only locationName has to be extracted
        String extractedLocationName = locationName.split(",")[0];
        LOGGER.info("extractedLocationName: " + extractedLocationName);

        locations = autocomplete(extractedLocationName);
        LOGGER.info("in getFirstMatch:");

        if (!locations.isEmpty()) {
            singleLocation = locations.get(0);
            setCoordinates();
            LOGGER.info(singleLocation.toDebugString());
        }
        else {
            LOGGER.info("location is empty!");
        }

    }

    public void setCoordinates() {
        if (singleLocation != null) {
            longitude = singleLocation.getLongitude();
            latitude  = singleLocation.getLatitude();
        }
        //TODO: error handling
    }

    //TODO: convert LocationEntity to .json string

    public Location getSingleLocation() {
        return singleLocation;
    }

    public void setSingleLocation(Location singleLocation) {
        this.singleLocation = singleLocation;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLocationName(String locationName) {
        if (Objects.equals(locationName, "")) {
            LOGGER.info("location name is empty!");
        }
        this.locationName = locationName;
    }

    public String getLocationName() {
        return this.locationName;
    }

    //only temporary
    public String getLocationResponse() {
        if (singleLocation == null) {
            LOGGER.info("singleLocation is empty");
            return null;
        }
        else {
            return singleLocation.toDebugString();
        }
    }
}
