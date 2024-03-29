package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.location.Location;

import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.services.MessageService;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller             // @Controller is a specification of @Component
@Scope("view")
public class SearchWeatherController {

    private static final Logger logger = LoggerFactory.getLogger(SearchWeatherController.class);
    @Autowired
    private WeatherController weatherController;
    @Autowired
    private LocationService locationService;
    @Autowired
    private MessageService messageService;
    private Location currentLocation;

    private String locationToSearch;


    /**
     * Initiates a weather search and redirection to the detail page based on user input.
     *
     * Calls the {@link #isLocationValid()} method to check if the entered location is valid.
     * If a valid location is found, it redirects to the detail page using {@link #redirectToDetailPage()}.
     */
    public void searchAndRedirect() {
        boolean wasLocationFound = isLocationValid();

        if (wasLocationFound) {
            redirectToDetailPage();
        }
    }


    /**
     * Searches for weather information based on the specified location.
     * If the location is found either in the database or by querying the location API,
     * weather information is retrieved and stored in the controller's fields.
     *
     * @return True if a valid location is found; otherwise, false.
     */
    public boolean isLocationValid() {
        try {
            if (locationToSearch == null || locationToSearch.trim().isEmpty()) {
                String warnMessage = "Please enter a city.";
                messageService.showInfoMessage(warnMessage);
                return false;
            }

            // retrieves location using databasea
            // when no location is found in db, service calls API
            Location singleLocation = locationService.retrieveLocation(locationToSearch);

            if (singleLocation != null) {
                currentLocation = singleLocation;
                return true;
            } else {
                handleNoLocationFound();
                return false;
            }
        } catch (ApiQueryException e) {
            logger.error("Error querying location API", e);
            String message = "An error occurred while processing the data!";
            messageService.showWarnMessage(message);
            return false;
        } catch (EmptyLocationException e) {
            String message = String.format("Location '%s' not found!", locationToSearch);
            messageService.showInfoMessage(message);
            return false;
        }
    }


    /**
     * Handles the scenario when no location is found, showing an info message and logging the event.
     */
    private void handleNoLocationFound() {
        String message = String.format("Cannot find city: %s", locationToSearch);
        messageService.showInfoMessage(message);
        logger.info("No location found for search: {}", locationToSearch);
    }


    private void redirectToDetailPage() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String url = externalContext.getRequestContextPath() + "/common/detail.xhtml?location=" + currentLocation.getName();
        try {
            externalContext.redirect(url);
        } catch (Exception e) {
            logger.error("Exception occurred in redirection: {}", e.getMessage());
        }
    }


    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }


    public String getLocationToSearch() {
        return locationToSearch;
    }

    @GetMapping(value = "/common/detail.xhtml")
    public void setLocationToSearch(@RequestParam("location") String locationToSearch) {
        this.locationToSearch = locationToSearch;
    }
}
