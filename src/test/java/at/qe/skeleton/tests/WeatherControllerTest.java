package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.WeatherController;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeatherControllerTest {

    @InjectMocks
    private WeatherController weatherController;

    @Mock
    private WeatherApiRequestService weatherApiRequestService;

    @Mock
    private WeatherService weatherService;

    private HourlyWeatherDTO hourlyWeatherDTO;
    private CurrentWeatherDTO currentWeatherDTO;
    private DailyWeatherDTO dailyWeatherDTO;
    private CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupDTOs();
    }

    @Test
    void testRequestWeather_SuccessfulApiCall() throws ApiQueryException {
        weatherController.setLongitude(currentAndForecastAnswerDTO.longitude());
        weatherController.setLatitude(currentAndForecastAnswerDTO.latitude());

        when(weatherApiRequestService.retrieveCurrentAndForecastWeather(weatherController.getLatitude(),
                weatherController.getLongitude()))
                .thenReturn(currentAndForecastAnswerDTO);

        weatherController.requestWeather();
        verify(weatherApiRequestService).retrieveCurrentAndForecastWeather(
                weatherController.getLatitude(), weatherController.getLongitude());

        assertEquals(currentAndForecastAnswerDTO, weatherController.getCurrentWeatherDTO());
    }

    public void setupDTOs() {
        HourlyWeatherDTO hourlyWeatherDTO = new HourlyWeatherDTO(
                Instant.now(),
                30, 30, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 0,
                null, null, null
        );

        dailyWeatherDTO = new DailyWeatherDTO(
                Instant.now(), Instant.now(), Instant.now(), Instant.now(), Instant.now(), 10.0,
                "null", null, null, 1, 1,
                1, 10, 10, 10, 10, 10, 0,
                null, null, null
        );

        currentWeatherDTO = new CurrentWeatherDTO(
                Instant.now(), Instant.now(), Instant.now(), 10, 10, 10,
                10, 10, 10, 10, 10, null, null, 10, 10,
                10, null
        );

        currentAndForecastAnswerDTO = new CurrentAndForecastAnswerDTO(
                47.0,
                13.0,
                "Vienna",
                3,
                currentWeatherDTO,
                null,
                List.of(hourlyWeatherDTO),
                List.of(dailyWeatherDTO),
                null
        );
    }

}
