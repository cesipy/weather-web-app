package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.CurrentlyHourlyDailyWeather;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherDataService;
import at.qe.skeleton.external.services.WeatherService;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.HourlyWeatherDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


public class WeatherServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceTest.class);
    @InjectMocks
    private WeatherService weatherService;
    @Mock
    private DailyWeatherDataRepository dailyWeatherDataRepository;

    @Mock
    private HourlyWeatherDataRepository hourlyWeatherDataRepository;

    @Mock
    private WeatherApiRequestService weatherApiRequestService;

    @Mock
    private WeatherDataService weatherDataService;

    @Mock
    private CurrentWeatherDataRepository currentWeatherDataRepository;

    private Location location;
    private HourlyWeatherDTO hourlyWeatherDTO;
    private DailyWeatherDTO dailyWeatherDTO;
    private CurrentWeatherDTO currentWeatherDTO;
    private CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        location = new Location();
        location.setName("Innsbruck");
        location.setLongitude(13.0);
        location.setLatitude(47.0);
        setupDTOs();
    }


    @Test
    public void testFetchCurrentWeatherData_DataIsStale() throws ApiQueryException {

        DailyWeatherData dailyWeatherData = new DailyWeatherData();
        HourlyWeatherData hourlyWeatherData = new HourlyWeatherData();
        dailyWeatherData.setLocation(location.getName());
        dailyWeatherData.setAdditionTime(Instant.now());
        hourlyWeatherData.setLocation(location.getName());
        hourlyWeatherData.setAdditionTime(Instant.now());

        when(dailyWeatherDataRepository.findLatestByLocation(ArgumentMatchers.anyString(), any()))
                .thenReturn(List.of(dailyWeatherData));
        when(hourlyWeatherDataRepository.findLatestByLocation(ArgumentMatchers.anyString(), any()))
                .thenReturn(List.of(hourlyWeatherData));

        CurrentlyHourlyDailyWeather result = weatherService.processWeatherForLocation(location);
        List<HourlyWeatherDTO> hourlyWeatherDTOS = result.getHourlyWeatherList();
        List<DailyWeatherDTO> dailyWeatherDTOS = result.getDailyWeatherList();


        // as there is no information about location saved in DTOS, it is only possible to test for length
        assertEquals(1, hourlyWeatherDTOS.size());
        assertEquals(1, dailyWeatherDTOS.size());
    }


   /** @Test
    public void testFetchCurrentWeatherAndForecast_FreshData() throws ApiQueryException {

        DailyWeatherData dailyWeatherData = new DailyWeatherData();
        HourlyWeatherData hourlyWeatherData = new HourlyWeatherData();
        dailyWeatherData.setLocation(location.getName());
        dailyWeatherData.setAdditionTime(Instant.now().minus(2, ChronoUnit.HOURS));
        hourlyWeatherData.setLocation(location.getName());
        hourlyWeatherData.setAdditionTime(Instant.now().minus(2, ChronoUnit.HOURS));

        when(dailyWeatherDataRepository.findLatestByLocation(anyString(), any()))
                .thenReturn(List.of(dailyWeatherData));
        when(hourlyWeatherDataRepository.findLatestByLocation(anyString(), any()))
                .thenReturn(List.of(hourlyWeatherData));

        when(weatherApiRequestService.retrieveCurrentAndForecastWeather(anyDouble(), anyDouble()))
                .thenReturn(currentAndForecastAnswerDTO);

        CurrentlyHourlyDailyWeather result = weatherService.processWeatherForLocation(location);

        assertNotNull(result);
        assertEquals(result.getDailyWeatherList(), List.of(dailyWeatherDTO));
        assertEquals(result.getHourlyWeatherList(), List.of(hourlyWeatherDTO));
    }


    @Test
    public void testFetchCurrentWeatherData_DataIsInDBButNotStale() throws ApiQueryException {

        DailyWeatherData dailyWeatherData = new DailyWeatherData();
        HourlyWeatherData hourlyWeatherData = new HourlyWeatherData();
        dailyWeatherData.setLocation(location.getName());
        dailyWeatherData.setAdditionTime(Instant.now().minus(2,ChronoUnit.HOURS));
        hourlyWeatherData.setLocation(location.getName());
        hourlyWeatherData.setAdditionTime(Instant.now().minus(2,ChronoUnit.HOURS));


        when(dailyWeatherDataRepository.findLatestByLocation(ArgumentMatchers.anyString(), any()))
                .thenReturn(List.of(dailyWeatherData));
        when(hourlyWeatherDataRepository.findLatestByLocation(ArgumentMatchers.anyString(), any()))
                .thenReturn(List.of(hourlyWeatherData));

        when(weatherApiRequestService.retrieveCurrentAndForecastWeather(anyDouble(), anyDouble()))
                .thenReturn(currentAndForecastAnswerDTO);


        CurrentlyHourlyDailyWeather result = weatherService.processWeatherForLocation(location);
        List<HourlyWeatherDTO> hourlyWeatherDTOS = result.getHourlyWeatherList();
        List<DailyWeatherDTO> dailyWeatherDTOS = result.getDailyWeatherList();


        // as there is no information about location saved in DTOS, it is only possible to test for length
        assertEquals(hourlyWeatherDTOS.size(), 1);
        assertEquals(dailyWeatherDTOS.size(), 1);
    }**/

    @Test
    public void testFetchCurrentWeather_WeatherIsStale() throws ApiQueryException {
        CurrentWeatherData currentWeatherData = new CurrentWeatherData();
        currentWeatherData.setAdditionTime(Instant.now());
        currentWeatherData.setLocation(location);
        currentWeatherData.setFeelsLikeTemperature(30);

        when(currentWeatherDataRepository.findByLocationOrderByAdditionTimeDesc(location))
                .thenReturn(List.of(currentWeatherData));

        CurrentWeatherData result = weatherService.fetchCurrentWeather(location);

        assertEquals(currentWeatherData, result);
    }

    @Test
    public void testFetchCurrentWeather_WeatherIsInDbButNotStale() throws ApiQueryException {
        CurrentWeatherData currentWeatherData = new CurrentWeatherData();
        currentWeatherData.setAdditionTime(Instant.now().minus(2, ChronoUnit.HOURS));
        currentWeatherData.setLocation(location);
        currentWeatherData.setFeelsLikeTemperature(30);


        when(currentWeatherDataRepository.findByLocationOrderByAdditionTimeDesc(location))
                .thenReturn(List.of(currentWeatherData));

        when(weatherApiRequestService.retrieveCurrentAndForecastWeather(location.getLatitude(), location.getLongitude()))
                .thenReturn(currentAndForecastAnswerDTO);

        CurrentWeatherData result = weatherService.fetchCurrentWeather(location);

        assertEquals(currentWeatherData, result);
    }


    public void setupDTOs() {
        hourlyWeatherDTO = new HourlyWeatherDTO(
                Instant.now(),
                30, 30, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 0, null, null, null
        );

        dailyWeatherDTO = new DailyWeatherDTO(
                Instant.now(), Instant.now(), Instant.now(), Instant.now(), Instant.now(), 10.0,
                "null", null, null, 1, 1,
                1 ,10, 10, 10, 10, 10, 0, null,
                null, null
        );

        currentWeatherDTO = new CurrentWeatherDTO(
                Instant.now(), Instant.now(), Instant.now(), 10, 10, 10,
                10, 10, 10 , 10, 10, null, null, 10, 10,
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
