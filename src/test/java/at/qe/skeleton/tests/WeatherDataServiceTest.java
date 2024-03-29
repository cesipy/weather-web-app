package at.qe.skeleton.tests;

import at.qe.skeleton.external.domain.DailyAggregationData;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.domain.TemperatureAggregationData;
import at.qe.skeleton.external.model.currentandforecast.misc.*;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.shared.WeatherDTO;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.external.services.WeatherDataService;
import at.qe.skeleton.external.repositories.DailyAggregationDataRepository;
import at.qe.skeleton.external.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.external.repositories.HourlyWeatherDataRepository;
import at.qe.skeleton.external.repositories.TemperatureAggregationDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

public class WeatherDataServiceTest {

    @InjectMocks
    WeatherDataService weatherDataService;

    @Mock
    DailyWeatherDataRepository dailyWeatherDataRepository;
    @Mock
    DailyAggregationDataRepository dailyAggregationDataRepository;

    @Mock
    TemperatureAggregationDataRepository temperatureAggregationDataRepository;
    @Mock
    HourlyWeatherDataRepository hourlyWeatherDataRepository;
    @Mock
    CurrentWeatherDataRepository currentWeatherDataRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveDailyWeatherFromDTO() {
        // Set up test data
        DailyWeatherDTO dailyWeatherDTO = new DailyWeatherDTO(
                null,
                null,
                null,
                null,
                null,
                1.2,
                "Test",
                new DailyTemperatureAggregationDTO(
                        10.2,
                        11.2,
                        8.3,
                        10.5,
                        2,
                        15
                ),
                new TemperatureAggregationDTO(
                        10.2,
                        11.2,
                        8.3,
                        10.5
                ),
                1,
                1,
                2.2,
                3.4,
                5.3,
                123.3,
                1,
                1,
                1,
                0.3,
                0.3,
                null
        );
        String location = "testLocation";
        DailyWeatherData data = new DailyWeatherData(null, null, null, null, null, 1.2, "Test",
                new DailyAggregationData(10.2, 11.2, 8.3, 10.5, 2, 15),
                new TemperatureAggregationData(10.2, 11.2, 8.3, 10.5), 1, 1, 2.2, 3.4, 5.3, 123.3, 1, 1, 1, 0.3, 0.3, Instant.now(), "testLocation");

        when(dailyWeatherDataRepository.findLatestByLocation(location, PageRequest.of(0, 1))).thenReturn(List.of(data));
        weatherDataService.saveDailyWeatherFromDTO(dailyWeatherDTO, location);

        assertEquals(dailyWeatherDTO, weatherDataService.convertDailyDataToDTO(dailyWeatherDataRepository.findLatestByLocation(location, PageRequest.of(0, 1)).get(0)));
    }
    @Test
    public void testConvertDailyDataToDTO() {

        DailyAggregationData dailyData = new DailyAggregationData(10,15,8,4,2,17);
        TemperatureAggregationData tempData = new TemperatureAggregationData(1000, 10, 15, 7);

        DailyWeatherData data = new DailyWeatherData();
        data.setDailyTemperatureAggregation(dailyData);
        data.setFeelsLikeTemperatureAggregation(tempData);

        DailyWeatherDTO result = weatherDataService.convertDailyDataToDTO(data);


        assertEquals(data.getTimestamp(), result.timestamp());
    }

    @Test
    public void testGetDailyAggregationFromDTO() {
        // Set up test data
        DailyTemperatureAggregationDTO dailyAggregationDTO = new DailyTemperatureAggregationDTO(
                10,
                15,
                8,
                4,
                2,
                17
        );

        DailyAggregationData result = weatherDataService.getDailyAggregationFromDTO(dailyAggregationDTO);

        verify(dailyAggregationDataRepository).save(any(DailyAggregationData.class));
        assertEquals(dailyAggregationDTO.morningTemperature(), result.getMorningTemperature());
    }

    @Test
    public void testConverDailyAggregationToDTO() {
        // Set up test data
        DailyAggregationData data = new DailyAggregationData(10,15,8,4,2,17);

        // Call the method under test
        DailyTemperatureAggregationDTO result = weatherDataService.converDailyAggregationToDTO(data);

        // Assert the result
        assertEquals(data.getMorningTemperature(), result.morningTemperature());
    }
    @Test
    public void testGetAggregationTemperatureFromDTO() {
        // Set up test data
        TemperatureAggregationDTO temperatureAggregationDTO = new TemperatureAggregationDTO(
                10,
                12,
                14,
                5
        );

        // Call the method under test
        TemperatureAggregationData result = weatherDataService.getAggregationTemperatureFromDTO(temperatureAggregationDTO);

        // Verify behaviors and assert the result
        verify(temperatureAggregationDataRepository).save(any(TemperatureAggregationData.class));
        assertEquals(temperatureAggregationDTO.morningTemperature(), result.getMorningTemperature());
    }

    @Test
    public void testConvertTempAggregationToDTO() {
        // Set up test data
        TemperatureAggregationData data = new TemperatureAggregationData();

        // Call the method under test
        TemperatureAggregationDTO result = weatherDataService.convertTempAggregationToDTO(data);

        // Assert the result
        assertEquals(data.getMorningTemperature(), result.morningTemperature());
    }

    @Test
    public void testConvertHourlyDataToDTO() {
        // Set up test data
        HourlyWeatherData data = new HourlyWeatherData();

        // Call the method under test
        HourlyWeatherDTO result = weatherDataService.convertHourlyDataToDTO(data);

        // Assert the result
        assertEquals(data.getTemperature(), result.temperature());
    }

    @Test
    public void testSaveHourlyWeatherFromDTO() {
        // Set up test data
        HourlyWeatherDTO hourlyWeatherDTO = new HourlyWeatherDTO(
                null,
                15.2,
                177.2,
                3,
                5.5,
                7.2,
                5,
                1,
                5,
                2.66,
                3.2,
                14.3,
                1,
                1.2,
                0.2,
                null
        );
        String location = "testLocation";
        HourlyWeatherData data = new HourlyWeatherData( null, 15.2, 177.2, 3, 5.5, 7.2, 5, 1, 5, 2.66, 3.2, 14.3, 1, 1.2, 0.2, Instant.now(), "testlocation");

        when(hourlyWeatherDataRepository.findLatestByLocation(location, PageRequest.of(0, 1))).thenReturn(List.of(data));

        weatherDataService.saveHourlyWeatherFromDTO(hourlyWeatherDTO, location);


        assertEquals(hourlyWeatherDTO, weatherDataService.convertHourlyDataToDTO(hourlyWeatherDataRepository.findLatestByLocation(location, PageRequest.of(0, 1)).get(0)));
    }
    @Test
    public void testSaveCurrentWeatherFromDTO() {
        // Set up test data
        CurrentWeatherDTO currentWeatherDTO = new CurrentWeatherDTO(
                null,
                null,
                null,
                10,
                12,
                1,
                12,
                0.0,
                1,
                1,
                1,
                null,
                null,
                2.2,
                3.2,
                123.3,
                new WeatherDTO(
                        100,
                        null,
                        null,
                        null
                )
        );
        Location location = new Location();

        // Call the method under test
        weatherDataService.saveCurrentWeatherFromDTO(currentWeatherDTO, location);

        // Verify behaviors
        verify(currentWeatherDataRepository).save(any(CurrentWeatherData.class));
    }

    @Test
    public void testConvertCurrentDataToDTO() {
        // Set up test data
        CurrentWeatherData currentWeatherData = new CurrentWeatherData();

        // Call the method under test
        CurrentWeatherDTO result = weatherDataService.convertCurrentDataToDTO(currentWeatherData);

        // Assert the result
        assertEquals(currentWeatherData.getTemperature(), result.temperature());
    }
}

