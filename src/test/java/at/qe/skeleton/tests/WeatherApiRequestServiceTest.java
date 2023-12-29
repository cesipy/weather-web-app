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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
public class WeatherApiRequestServiceTest {

    @Autowired
    private WeatherApiRequestService weatherApiRequestService;

    @Autowired
    private RestClient restClient;

    @Test
    public void testRetrieveLocation() {
        double latitude = 47.2692;
        double longitude = 11.4041;

        CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO =
                weatherApiRequestService.retrieveCurrentAndForecastWeather(latitude, longitude);

        assertEquals(latitude, currentAndForecastAnswerDTO.latitude(), "is api's latitude the same to hard coded one?");
    }
}
