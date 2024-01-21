package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.location.Location;

import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.services.WeatherService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller             // @Controller is a specification of @Component
@Scope("view")
public class SearchWeatherController {

    private static final Logger logger = LoggerFactory.getLogger(SearchWeatherController.class);
    @Autowired
    private WeatherController weatherController;
    @Autowired
    private LocationService locationService;
    private CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO;
    private String currentWeather;
    private Location currentLocation;
    private String currentLocationString;
    private String locationToSearch;

    @Autowired
    private WeatherService weatherService;
    private HourlyWeatherDTO hourlyWeatherDTO;
    private HourlyWeatherDTO weatherInOneHour;
    private List<HourlyWeatherDTO> hourlyWeatherList;
    private List<DailyWeatherDTO> dailyWeatherList;


    public void searchAndRedirect() {
        searchWeatherByLocation();

        logger.info("before redirect");
        redirectToDetailPage();
        logger.info("after redircet");
    }

    /**
     * Searches for weather information based on the specified location.
     * If the location is found either in the database or by querying the location API,
     * weather information is retrieved and stored in the controller's fields.
     */
    public void searchWeatherByLocation() {
        try {
            if (locationToSearch == null || locationToSearch.trim().isEmpty()) {
                String warnMessage = "Please enter a city.";
                showInfoMessage(warnMessage);
                return;
            }

            // retrieves location using database
            // when no location is found in db, service calls API
            Location singleLocation = locationService.retrieveLocation(locationToSearch);

            if (singleLocation != null) {
                currentLocation = singleLocation;
                processWeatherForLocation(singleLocation);
            } else {
                handleNoLocationFound();
            }
        } catch (ApiQueryException e) {
            logger.error("Error querying location API", e);
            showWarnMessage();
        } catch (EmptyLocationException e) {
            String message = "Cannot find city: %s".formatted(locationToSearch);
            showInfoMessage(message);
        }
    }


    /**
     * Handles the scenario when no location is found, showing an info message and logging the event.
     */
    private void handleNoLocationFound() {
        String message = String.format("Cannot find city: %s", locationToSearch);
        showInfoMessage(message);
        logger.info("No location found for search: {}", locationToSearch);
    }


    // TODO: wetter muss anders prozessiert werden, nicht von controller, sondern von weatherService
    /**
     * Processes weather information for a given location.
     * The location details are set in the controller, and the weather is retrieved using the {@link WeatherController}.
     *
     * @param singleLocation location for which weather information is to be retrieved
     */
    public void processWeatherForLocation(Location singleLocation) {
        setCurrentLocation(singleLocation);
        setCurrentLocationString(singleLocation.toDebugString());

        weatherController.setLatitude(singleLocation.getLatitude());
        weatherController.setLongitude(singleLocation.getLongitude());

        weatherController.requestWeather();
        setCurrentAndForecastAnswerDTO(weatherController.getCurrentWeatherDTO());
        setCurrentWeather(weatherController.getCurrentWeather());

        logger.info(String.valueOf(currentAndForecastAnswerDTO));
        //todo: only temp exception handling here:
        try {

            CurrentlyHourlyDailyWeather weatherData =  weatherService.processWeatherForLocation(singleLocation);
            hourlyWeatherList = weatherData.getHourlyWeatherList();
            dailyWeatherList  = weatherData.getDailyWeatherList();

            logger.info(dailyWeatherList.toString());
            logger.info(hourlyWeatherList.toString());

        }
        catch (Exception e) {
            logger.info("exception in processWeather, {}", e.getMessage());
        }

    }

    private void redirectToDetailPage() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String url = externalContext.getRequestContextPath() + "/secured/detail.xhtml?location=" + currentLocation.getName();
        logger.info(url);
        try {
            externalContext.redirect(url);
        } catch (Exception e) {
            logger.error("Exception occurred in redirection: {}", e.getMessage());
        }
    }



    /**
     * Displays a warning message about a location not being found.
     *
     */
    public void showWarnMessage() {
        String message = "An error occurred!";
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning:", message));
    }

    /**
     * Displays an informational message about a location not being found.
     *
     * @param locationName The name of the location for which the message is generated.
     */
    public void showInfoMessage(String locationName) {
        String message = String.format("Location '%s' not found!", locationName);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info:", message));
    }

    public CurrentAndForecastAnswerDTO getCurrentAndForecastAnswerDTO() {
        return currentAndForecastAnswerDTO;
    }

    public void setCurrentAndForecastAnswerDTO(CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO) {
        this.currentAndForecastAnswerDTO = currentAndForecastAnswerDTO;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String currentWeatherString) {
        this.currentWeather = currentWeatherString;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getCurrentLocationString() {
        return (currentLocationString == null) ? "no location found" : currentLocationString;
    }

    public void setCurrentLocationString(String currentLocationString) {
        this.currentLocationString = currentLocationString;
    }

    public String getLocationToSearch() {
        return locationToSearch;
    }

    @RequestMapping(value = "/secured/detail.xhtml", method = RequestMethod.GET)
    public void setLocationToSearch(@RequestParam("location") String locationToSearch) {
        this.locationToSearch = locationToSearch;
    }

    public HourlyWeatherDTO getHourlyWeatherDTO() {
        return hourlyWeatherDTO;
    }

    public void setHourlyWeatherDTO(HourlyWeatherDTO hourlyWeatherDTO) {
        this.hourlyWeatherDTO = hourlyWeatherDTO;
    }

    public HourlyWeatherDTO getWeatherInOneHour() {
        return weatherInOneHour;
    }

    public void setWeatherInOneHour(HourlyWeatherDTO weatherInOneHour) {
        this.weatherInOneHour = weatherInOneHour;
    }

    public List<HourlyWeatherDTO> getHourlyWeatherList() {
        logger.info("in detailed view: {}", hourlyWeatherList);
        return hourlyWeatherList;
    }

    public void setHourlyWeatherList(List<HourlyWeatherDTO> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }

    public List<DailyWeatherDTO> getDailyWeatherList() {
        return dailyWeatherList;
    }

    public void setDailyWeatherList(List<DailyWeatherDTO> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }
}
