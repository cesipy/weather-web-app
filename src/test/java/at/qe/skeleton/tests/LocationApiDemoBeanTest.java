package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.misc.*;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.*;
import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.shared.WeatherDTO;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationApiRequestService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherDataService;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.HourlyWeatherDataRepository;
import at.qe.skeleton.internal.ui.beans.DiagramBean;
import at.qe.skeleton.internal.ui.beans.LocationApiDemoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.charts.line.LineChartModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationApiDemoBeanTest {

    @InjectMocks
    LocationApiDemoBean locationApiDemoBean;

    @Mock
    LocationApiRequestService locationApiRequestService;
    @Mock
    WeatherApiRequestService weatherApiRequestService;

    @Mock
    DailyWeatherDataRepository dailyWeatherDataRepository;

    @Mock
    HourlyWeatherDataRepository hourlyWeatherDataRepository;

    @Mock
    WeatherDataService weatherDataService;
    @Mock
    DiagramBean diagramBean;
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInit() throws ApiQueryException, EmptyLocationException {
        // Set up test data
        String queryName = "Innsbruck";
        locationApiDemoBean.setQuery_name(queryName);

        List<LocationDTO> locations = new ArrayList<>();
        LocationDTO location = new LocationDTO(
          "Innsbruck",
          null,
                47.2692,
                11.4041,
                "AT",
                "TY"
        );
        locations.add(location);
        List<PrecipitationByTimestampDTO> list = new ArrayList<>();
        list.add(new PrecipitationByTimestampDTO(
                null,
                0.0
        ));
        List<HourlyWeatherDTO> list2 = new ArrayList<>();
        list2.add(new HourlyWeatherDTO(
                        null,
                        10.2,
                        12.2,
                        1,
                        3.5,
                        2.2,
                        1,
                        1,
                        1,
                        2.2,
                        3.4,
                        132.3,
                        1,
                        0.2,
                        0.2,
                        new WeatherDTO(
                                100,
                                null,
                                null,
                                null
                        )
                ));
        List<DailyWeatherDTO> list3 = new ArrayList<>();
        list3.add(new DailyWeatherDTO(
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
        ));
        CurrentAndForecastAnswerDTO forecastAnswer = new CurrentAndForecastAnswerDTO(
                47.2692,
                11.4041,
                null,
                0,
                new CurrentWeatherDTO(
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
                ),
                list,
                list2,
                list3,
                null
        );
        Pageable last_entry = PageRequest.of(0, 8);
        Pageable last_entry2 = PageRequest.of(0, 2);

        List<DailyWeatherData> dailyWeatherDataList = new ArrayList<>();
        List<HourlyWeatherData> hourlyWeatherDataList = new ArrayList<>();

        when(weatherApiRequestService.retrieveCurrentAndForecastWeather(location.latitude(), location.longitude())).thenReturn(forecastAnswer);
        when(locationApiRequestService.retrieveLocations(queryName, 1)).thenReturn(locations);
        when(dailyWeatherDataRepository.findLatestByLocation(location.name(), last_entry)).thenReturn(dailyWeatherDataList);
        when(hourlyWeatherDataRepository.findLatestByLocation(location.name(), last_entry2)).thenReturn(hourlyWeatherDataList);

        // Call the method under test
        locationApiDemoBean.init();

        // Verify behaviors
        verify(locationApiRequestService).retrieveLocations(queryName, 1);
        verify(weatherApiRequestService).retrieveCurrentAndForecastWeather(location.latitude(), location.longitude());
        verify(dailyWeatherDataRepository).findLatestByLocation(location.name(), last_entry);
        verify(hourlyWeatherDataRepository).findLatestByLocation(location.name(), last_entry2);
        // Add more verification statements as needed
    }
    @Test
    public void testOnStartDateSelect() {
        // Set up test data
        Date startDate = new Date();
        SelectEvent event = mock(SelectEvent.class);
        when(event.getObject()).thenReturn(startDate);

        // Call the method under test
        locationApiDemoBean.onStartDateSelect(event);

        // Verify behaviors and assert values
        verify(event).getObject();

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, 14);
        Date expectedEndDateMax = c.getTime();

        assertEquals(expectedEndDateMax, locationApiDemoBean.getEnd_date_max());
    }

    @Test
    public void testGetHolidayForecast() {
        // Set up test data
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + (1000 * 60 * 60 * 24 * 7)); // One week later

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        locationApiDemoBean.setStart_date(startDate);
        locationApiDemoBean.setEnd_date(endDate);

        LocationDTO location = new LocationDTO(
                "Innsbruck",
                null,
                47.2692,
                11.4041,
                "AT",
                "TY"
        );

        List<HolidayDTO> holidays = new ArrayList<>();
        HolidayDTO holiday = new HolidayDTO(
                47.2692,
                11.4041,
                null,
                null,
                null,
                new CloudDTO(
                        1
                ),
                new HumidityDTO(
                        1
                ),
                new PrecipitationDTO(
                        1
                ),
                new PressureDTO(
                        1
                ),
                new DailyTemperatureAggregationDTO(
                        10,
                        11,
                        12,
                        13,
                        9,
                        14
                ),
                new WindDTO(
                        new MaxDTO(
                                1,
                                123
                        )
                )
        );
        holidays.add(holiday);

        // Mock behaviors
        when(weatherApiRequestService.retrieveDailyHolidayForecast(anyDouble(), anyDouble(), anyString())).thenReturn(holiday);

        // Call the method under test
        locationApiDemoBean.setLocation(location);
        locationApiDemoBean.getHolidayForecast();

        // Verify behaviors
        verify(weatherApiRequestService, times((13))).retrieveDailyHolidayForecast(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble(), ArgumentMatchers.anyString());
    }

    @Test
    public void testGetPastAverageForDateRange() {
        // Set up test data
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + (1000 * 60 * 60 * 24 * 7)); // One week later
        locationApiDemoBean.setStart_date(startDate);
        locationApiDemoBean.setEnd_date(endDate);
        LocationDTO location = new LocationDTO(
                "Paris",
                null,
                48.856,
                2.3522,
                "FR",
                "PR"
        );

        HolidayDTO holiday = new HolidayDTO(
                47.2692,
                11.4041,
                null,
                null,
                null,
                new CloudDTO(
                        1
                ),
                new HumidityDTO(
                        1
                ),
                new PrecipitationDTO(
                        1
                ),
                new PressureDTO(
                        1
                ),
                new DailyTemperatureAggregationDTO(
                        10,
                        11,
                        12,
                        13,
                        9,
                        14
                ),
                new WindDTO(
                        new MaxDTO(
                                1,
                                123
                        )
                )
        );

        // Mock behaviors
        when(weatherApiRequestService.retrieveDailyHolidayForecast(anyDouble(), anyDouble(), anyString())).thenReturn(holiday);

        // Call the method under test
        locationApiDemoBean.setLocation(location);
        locationApiDemoBean.getPastAverageForDateRange();

        // Verify behaviors
        verify(weatherApiRequestService, times(5)).retrieveDailyHolidayForecast(anyDouble(), anyDouble(), anyString());
    }

        @Test
        public void testGettersAndSetters() {
            // Set up test data
            LocationDTO currentLocation = new LocationDTO(
                    "Paris",
                    null,
                    48.856,
                    2.3522,
                    "FR",
                    "PR"
            );
            HourlyWeatherDTO currentWeather = new HourlyWeatherDTO(
                    null,
                    10.2,
                    12.2,
                    1,
                    3.5,
                    2.2,
                    1,
                    1,
                    1,
                    2.2,
                    3.4,
                    132.3,
                    1,
                    0.2,
                    0.2,
                    new WeatherDTO(
                            100,
                            null,
                            null,
                            null
                    )
            );
            HourlyWeatherDTO weatherInOneHour = new HourlyWeatherDTO(
                    null,
                    144.2,
                    13.2,
                    1,
                    3.5,
                    2.2,
                    1,
                    1,
                    1,
                    2.3,
                    3.5,
                    112.3,
                    87,
                    0.5,
                    0.7,
                    new WeatherDTO(
                            100,
                            null,
                            null,
                            null
                    )
            );
            List<HourlyWeatherDTO> hourlyWeatherList = new ArrayList<>();
            hourlyWeatherList.add(new HourlyWeatherDTO(
                    null,
                    13.2,
                    16.2,
                    2,
                    3.5,
                    5.2,
                    2,
                    6,
                    21,
                    12.2,
                    32.4,
                    92.3,
                    1,
                    0.6,
                    0.2,
                    new WeatherDTO(
                            100,
                            null,
                            null,
                            null
                    )
            ));
            List<DailyWeatherDTO> dailyWeatherList = new ArrayList<>();
            dailyWeatherList.add(new DailyWeatherDTO(
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
            ));
            List<HolidayDTO> holidayWeatherList = new ArrayList<>();
            holidayWeatherList.add(new HolidayDTO(
                    47.2692,
                    11.4041,
                    null,
                    null,
                    null,
                    new CloudDTO(
                            1
                    ),
                    new HumidityDTO(
                            1
                    ),
                    new PrecipitationDTO(
                            1
                    ),
                    new PressureDTO(
                            1
                    ),
                    new DailyTemperatureAggregationDTO(
                            10,
                            11,
                            12,
                            13,
                            9,
                            14
                    ),
                    new WindDTO(
                            new MaxDTO(
                                    1,
                                    123
                            )
                    )
            ));
            HolidayDTO pastAverage = new HolidayDTO(
                    47.2692,
                    11.4041,
                    null,
                    null,
                    null,
                    new CloudDTO(
                            1
                    ),
                    new HumidityDTO(
                            1
                    ),
                    new PrecipitationDTO(
                            1
                    ),
                    new PressureDTO(
                            1
                    ),
                    new DailyTemperatureAggregationDTO(
                            10,
                            11,
                            12,
                            13,
                            9,
                            14
                    ),
                    new WindDTO(
                            new MaxDTO(
                                    1,
                                    123
                            )
                    )
            );
            Date oneYearFromToday = new Date();
            Date start_date = new Date();
            Date end_date = new Date();
            Date end_date_max = new Date();
            String query_name = "testQuery";
            boolean modelReady = true;

            // Call the setters
            locationApiDemoBean.setLocation(currentLocation);
            locationApiDemoBean.setCurrentWeather(currentWeather);
            locationApiDemoBean.setWeatherInOneHour(weatherInOneHour);
            locationApiDemoBean.setHourlyWeatherList(hourlyWeatherList);
            locationApiDemoBean.setDailyWeatherList(dailyWeatherList);
            locationApiDemoBean.setHolidayWeatherList(holidayWeatherList);
            locationApiDemoBean.setPastAverage(pastAverage);
            locationApiDemoBean.setOneYearFromToday(oneYearFromToday);
            locationApiDemoBean.setStart_date(start_date);
            locationApiDemoBean.setEnd_date(end_date);
            locationApiDemoBean.setEnd_date_max(end_date_max);
            locationApiDemoBean.setQuery_name(query_name);
            locationApiDemoBean.setModelReady(modelReady);

            // Verify the getters
            assertEquals(currentLocation, locationApiDemoBean.getCurrentLocation());
            assertEquals(currentWeather, locationApiDemoBean.getCurrentWeather());
            assertEquals(weatherInOneHour, locationApiDemoBean.getWeatherInOneHour());
            assertEquals(hourlyWeatherList, locationApiDemoBean.getHourlyWeatherList());
            assertEquals(dailyWeatherList, locationApiDemoBean.getDailyWeatherList());
            assertEquals(holidayWeatherList, locationApiDemoBean.getHolidayWeatherList());
            assertEquals(pastAverage, locationApiDemoBean.getPastAverage());
            assertEquals(oneYearFromToday, locationApiDemoBean.getOneYearFromToday());
            assertEquals(start_date, locationApiDemoBean.getStart_date());
            assertEquals(end_date, locationApiDemoBean.getEnd_date());
            assertEquals(end_date_max, locationApiDemoBean.getEnd_date_max());
            assertEquals(query_name, locationApiDemoBean.getQuery_name());
            assertEquals(modelReady, locationApiDemoBean.getModelReady());
        }
    }

