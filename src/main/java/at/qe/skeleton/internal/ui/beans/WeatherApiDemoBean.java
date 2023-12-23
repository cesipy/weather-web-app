package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.services.WeatherApiRequestService;
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

@Component
@Scope("view")
public class WeatherApiDemoBean {

    @Autowired
    private WeatherApiRequestService weatherApiRequestService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiDemoBean.class);

    private Map<String, CurrentWeatherDTO> locationWeatherMap = new HashMap<>();

    // List of coordinates
    private List<double[]> coordinates = List.of(
            new double[]{47.2692, 11.4041}, // Innsbruck
            new double[]{48.8566, 2.3522}, // Paris
            new double[]{51.5074, -0.1278}, // London
            new double[]{40.7128, -74.0060}, // New York
            new double[]{34.0522, -118.2437}, // Los Angeles
            new double[]{35.6895, 139.6917}, // Tokyo
            new double[]{39.9042, 116.4074}, // Beijing
            new double[]{28.6139, 77.2090}, // New Delhi
            new double[]{-33.8688, 151.2093}, // Sydney
            new double[]{-23.5505, -46.6333} // Sao Paulo
    );


    @PostConstruct
    public void init() {
        for (double[] coordinate : coordinates) {
            try {
                CurrentAndForecastAnswerDTO answer = this.weatherApiRequestService.retrieveCurrentAndForecastWeather(coordinate[0], coordinate[1]);
                String locationName = this.weatherApiRequestService.getLocationName(coordinate[0], coordinate[1]);

                CurrentWeatherDTO currentWeather = answer.currentWeather();


                locationWeatherMap.put(locationName, currentWeather);
            } catch (final Exception e) {
                LOGGER.error("error in request", e);
            }
        }
    }

    public Map<String, CurrentWeatherDTO> getLocationWeatherMap() {
        return locationWeatherMap;
    }



}
