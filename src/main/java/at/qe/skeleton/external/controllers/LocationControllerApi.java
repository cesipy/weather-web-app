package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.services.LocationApiRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import java.util.List;

@Controller // @Controller is a specification of @Component
@Scope("view")
public class LocationControllerApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationControllerApi.class);
    private final int LIMIT = 1;        // we want to get only one result for a location
    private String locationName;
    private String currentLocation;
    private LocationDTO currentLocationDTO;
    @Autowired
    private LocationApiRequestService locationApiRequestService;

    public void search() {
        try {
            searchLocation(locationName);           // perform searching for location

            LocationDTO foundLocation = currentLocationDTO;

            FacesMessage message;
            if (foundLocation != null) {
                message = new FacesMessage("Location found!", "Name: " + foundLocation.name() +
                        ", Lat&Lon: " + foundLocation.latitude() + ", " + foundLocation.longitude());
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Location not found!", null);
            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            LOGGER.error("Error in location search", e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in location search", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    private void searchLocation(String locationName) {
        try {
            List<LocationDTO> answer = this.locationApiRequestService.retrieveLocations(locationName, LIMIT);

            // Check if the list is not empty
            if (!answer.isEmpty()) {
                // only process the first entry in the List of LocationDTOs
                LocationDTO firstLocation = answer.get(0);

                ObjectMapper mapper = new ObjectMapper()
                        .findAndRegisterModules()
                        .enable(SerializationFeature.INDENT_OUTPUT);

                String plainTextAnswer = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(firstLocation);
                String escapedHtmlAnswer = StringEscapeUtils.escapeHtml4(plainTextAnswer);
                String escapedHtmlAnswerWithLineBreaks = escapedHtmlAnswer.replace("\n", "<br>")
                        .replace(" ", "&nbsp;");
                this.setCurrentLocation(escapedHtmlAnswerWithLineBreaks);
                this.setCurrentLocationDTO(firstLocation);

                LOGGER.info("current location: " + currentLocation);

            } else {
                LOGGER.warn("The list of locations is empty.");
                // TODO: Error message when no location is found
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Error in request in locationApi", e);
            throw new RuntimeException(e);
        }
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

    public LocationDTO getCurrentLocationDTO() {
        return currentLocationDTO;
    }

    public void setCurrentLocationDTO(LocationDTO locationDTO) {
        this.currentLocationDTO = locationDTO;
    }

    public double getLatitude() {
        return currentLocationDTO.latitude();
    }

    public double getLongitude() {
        return currentLocationDTO.longitude();
    }
}
