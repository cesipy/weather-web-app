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
import at.qe.skeleton.internal.repositories.DailyAggregationDataRepository;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.HourlyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.TemperatureAggregationDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
                new WeatherDTO(
                        100,
                        null,
                        null,
                        null
                )
        );
        String location = "testLocation";

        // Call the method under test
        weatherDataService.saveDailyWeatherFromDTO(dailyWeatherDTO, location);

        // Verify behaviors
        verify(dailyWeatherDataRepository).save(any(DailyWeatherData.class));
    }
    @Test
    public void testConvertDailyDataToDTO() {
        // Set up test data
        DailyAggregationData dailyData = new DailyAggregationData(10,15,8,4,2,17);
        TemperatureAggregationData tempData = new TemperatureAggregationData(1000, 10, 15, 7);

        DailyWeatherData data = new DailyWeatherData();
        data.setDailyTemperatureAggregation(dailyData);
        data.setFeelsLikeTemperatureAggregation(tempData);
        // Call the method under test
        DailyWeatherDTO result = weatherDataService.convertDailyDataToDTO(data);

        // Assert the result
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

        // Call the method under test
        DailyAggregationData result = weatherDataService.getDailyAggregationFromDTO(dailyAggregationDTO);

        // Verify behaviors and assert the result
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
                new WeatherDTO(
                        100,
                        null,
                        null,
                        null
                )
        );
        String location = "testLocation";

        // Call the method under test
        weatherDataService.saveHourlyWeatherFromDTO(hourlyWeatherDTO, location);

        // Verify behaviors
        verify(hourlyWeatherDataRepository).save(any(HourlyWeatherData.class));
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

