package at.qe.skeleton.tests;


import at.qe.skeleton.configs.ApiConfiguration;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
public class WeatherApiRequestServiceTest {

    @Autowired
    private WeatherApiRequestService weatherApiRequestService;

    @Autowired
    private RestClient restClient;

    @Test
    public void testWeatherForecast() {
        double latitude = 47.2692;
        double longitude = 11.4041;

        double latitude1 = 51.5072;
        double longitude1 = -0.1276;

        double latitude2 = 39.7392;
        double longitude2 = -104.9903;

        double latitude3 = -34.6037;
        double longitude3 = -58.3816;

        CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO =
                weatherApiRequestService.retrieveCurrentAndForecastWeather(latitude, longitude);

        CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO1 =
                weatherApiRequestService.retrieveCurrentAndForecastWeather(latitude1, longitude1);

        CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO2 =
                weatherApiRequestService.retrieveCurrentAndForecastWeather(latitude2, longitude2);

        CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO3 =
                weatherApiRequestService.retrieveCurrentAndForecastWeather(latitude3, longitude3);

        assertEquals(latitude, currentAndForecastAnswerDTO.latitude(), "is api's latitude the same to hard coded one?");
        assertEquals(latitude1, currentAndForecastAnswerDTO1.latitude(), "is api's latitude the same to hard coded one?");
        assertEquals(latitude2, currentAndForecastAnswerDTO2.latitude(), "is api's latitude the same to hard coded one?");
        assertEquals(latitude3, currentAndForecastAnswerDTO3.latitude(), "is api's latitude the same to hard coded one?");

        assertEquals(longitude, currentAndForecastAnswerDTO.longitude(), "is api's longitude the same to hard coded one?");
        assertEquals(longitude1, currentAndForecastAnswerDTO1.longitude(), "is api's longitude the same to hard coded one?");
        assertEquals(longitude2, currentAndForecastAnswerDTO2.longitude(), "is api's longitude the same to hard coded one?");
        assertEquals(longitude3, currentAndForecastAnswerDTO3.longitude(), "is api's longitude the same to hard coded one?");
    }

    @Test
    public void testRetrieveLocation(){
        List<double[]> coordinates = List.of(
                new double[]{47.2692, 11.4041}, // Innsbruck
                new double[]{48.8566, 2.3522}, // Paris
                new double[]{51.5074, -0.1278}, // London
                new double[]{40.7127281, -74.0060152}, // New York County
                new double[]{34.0522, -118.2437}, // Los Angeles
                new double[]{35.6828387, 139.7594549}, // Chiyoda
                new double[]{39.9042, 116.4074} // Beijing
        );
        ArrayList<String> locationNames = new ArrayList<String>();
        for (double[] coordinate : coordinates) {
            locationNames.add(this.weatherApiRequestService.getLocationName(coordinate[0], coordinate[1]));
        }
        assertEquals("Innsbruck", locationNames.get(0), "is api's location name the same to hard coded one?");
        assertEquals("Paris", locationNames.get(1), "is api's location name the same to hard coded one?");
        assertEquals("London", locationNames.get(2), "is api's location name the same to hard coded one?");
        assertEquals("New York County", locationNames.get(3), "is api's location name the same to hard coded one?");
        assertEquals("Los Angeles", locationNames.get(4), "is api's location name the same to hard coded one?");
        assertEquals("Chiyoda", locationNames.get(5), "is api's location name the same to hard coded one?");
        assertEquals("Beijing", locationNames.get(6), "is api's location name the same to hard coded one?");

    }
}
