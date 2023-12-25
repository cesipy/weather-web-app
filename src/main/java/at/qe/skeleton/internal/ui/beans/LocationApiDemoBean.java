package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.services.LocationApiRequestService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Component
@Scope("view")
public class LocationApiDemoBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationApiDemoBean.class);

    @Autowired
    private LocationApiRequestService locationApiRequestService;

    @Autowired
    private WeatherApiRequestService weatherApiRequestService;

    private LocationDTO currentLocation;
    private CurrentWeatherDTO currentWeather;
    private String query_name;
    private final int LIMIT = 1;


    @PostConstruct
    public void init() {
        try {
            List<LocationDTO> answer = this.locationApiRequestService.retrieveLocations(getQuery_name(), getLIMIT());

            // Check if the list is not empty
            if (!answer.isEmpty()) {
                // only process first entry in List of LocationDTOs
                LocationDTO firstLocation = answer.get(0);

                this.setLocation(firstLocation);
                LOGGER.info("using coordinates: " + firstLocation.latitude() + " " + firstLocation.longitude());

                CurrentAndForecastAnswerDTO forecastAnswer = this.weatherApiRequestService.retrieveCurrentAndForecastWeather(firstLocation.latitude(), firstLocation.longitude());
                this.setCurrentWeather(forecastAnswer.currentWeather());
                System.out.println(firstLocation.latitude() + " " + firstLocation.longitude() + "Hello!");

                LOGGER.info("got this current weather " + currentWeather);

                //LOGGER.info("current location: " + currentLocation);
            } else {
                LOGGER.warn("The list of locations is empty.");
            }
        } catch (Exception e) {
            LOGGER.error("error in request in locationApi", e);
            throw new RuntimeException(e);
        }
    }



    public String getQuery_name() {
        return query_name;
    }

    public void setQuery_name(String query_name) {
        this.query_name = query_name;
    }

    public int getLIMIT() {
        return LIMIT;
    }

    public void setLocation(LocationDTO location) {
        this.currentLocation = location;
    }

    public LocationDTO getCurrentLocation() {
        return currentLocation;
    }

    public CurrentWeatherDTO getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeatherDTO currentWeather) {
        this.currentWeather = currentWeather;
    }

    public List<CurrentWeatherDTO> getCurrentWeatherAsList() {
        if (currentWeather != null) {
            return Collections.singletonList(currentWeather);
        } else {
            return Collections.emptyList();
        }
    }


}

