package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.Location;

import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationApiRequestService;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.internal.ui.beans.LocationApiDemoBean;
import at.qe.skeleton.internal.ui.beans.WeatherApiDemoBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.util.List;

@Controller // @Controller is a specification of @Component
@Scope("view")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    private final int LIMIT = 1;        // we want to get only one result for a location
    private String locationName;
    private String currentLocation;
    private Location currentLocationEntity;
    @Autowired
    private LocationApiRequestService locationApiRequestService;
    @Autowired
    private LocationApiDemoBean locationApiDemoBean;
    @Autowired
    private WeatherApiDemoBean weatherApiDemoBean;

    @Autowired
    private LocationService locationService;

    public void search() {
        try {
            searchLocation(locationName);           // perform searching for location

            Location foundLocation = currentLocationEntity;

            //TODO: handling for no location found
            //TODO: handle other errors

        } catch (Exception e) {
            logger.error("Error in location search", e);
        }
    }

    private void searchLocation(String locationName) {
        try {
            List<LocationDTO> answer = this.locationApiRequestService.retrieveLocations(locationName, LIMIT);

            // Check if the list is not empty
            if (!answer.isEmpty()) {
                // only process the first entry in the List of LocationDTOs

                Location firstLocation = locationApiRequestService.convertLocationDTOtoLocation(answer.get(0));


                ObjectMapper mapper = new ObjectMapper()
                        .findAndRegisterModules()
                        .enable(SerializationFeature.INDENT_OUTPUT);

                String plainTextAnswer = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(firstLocation);
                String escapedHtmlAnswer = StringEscapeUtils.escapeHtml4(plainTextAnswer);
                String escapedHtmlAnswerWithLineBreaks = escapedHtmlAnswer.replace("\n", "<br>")
                        .replace(" ", "&nbsp;");
                this.setCurrentLocation(escapedHtmlAnswerWithLineBreaks);
                this.setCurrentLocationEntity(firstLocation);

            } else {
                logger.warn("The list of locations is empty.");
                // TODO: Error message when no location is found
            }
        } catch (JsonProcessingException e) {
            logger.error("Error in request in locationApi", e);
            // TODO better handling of Exception
            throw new RuntimeException(e);

        } catch (ApiQueryException | EmptyLocationException e) {
            logger.info(e.getMessage());
        }
    }

    @GetMapping("/location")
    public Location getLocation(@RequestParam String location) {
        locationApiDemoBean.setQuery_name(location);
        locationApiDemoBean.init();
        return locationApiDemoBean.getCurrentLocation();
    }

    @GetMapping("/locations")
    public String getAllLocations(Model model) {
        model.addAttribute("locations", locationService.getAllLocations());
        return "weather_api_demo";
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getLIMIT() {
        return LIMIT;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }


    public double getLatitude() {
        return currentLocationEntity.getLatitude();
    }

    public double getLongitude() {
        return currentLocationEntity.getLongitude();
    }

    public Location getCurrentLocationEntity() {
        return currentLocationEntity;
    }

    public void setCurrentLocationEntity(Location currentLocationEntity) {
        this.currentLocationEntity = currentLocationEntity;
    }
}
