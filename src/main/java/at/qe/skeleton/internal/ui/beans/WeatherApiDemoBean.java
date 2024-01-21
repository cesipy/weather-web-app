package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.model.location.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


/**
 * Bean serving as the overview.
 * Retrieves all locations from the database for the purpose of displaying them on the website.
 */

@Component
@Scope("view")
public class WeatherApiDemoBean {

    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    @Autowired
    private LocationService locationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiDemoBean.class);

    private List<Location> locations;


    @PostConstruct
    public void init() {
        try {
            locations = locationService.getAllLocations();
        } catch (final Exception e) {
            LOGGER.error("error in request", e);
        }

    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
