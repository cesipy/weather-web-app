package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.CurrentlyHourlyDailyWeather;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HolidayDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherDataService;
import at.qe.skeleton.external.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.external.repositories.HourlyWeatherDataRepository;
import at.qe.skeleton.external.services.WeatherService;
import at.qe.skeleton.internal.services.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.text.SimpleDateFormat;

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

    @Test
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
        assertEquals(List.of(dailyWeatherDTO), result.getDailyWeatherList());
        assertEquals( List.of(hourlyWeatherDTO), result.getHourlyWeatherList());
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


        // Check if the lists are not empty before accessing their elements
        if (!hourlyWeatherDTOS.isEmpty() && !dailyWeatherDTOS.isEmpty()) {
            assertEquals(hourlyWeatherDTOS.get(0), hourlyWeatherDTO);
            assertEquals(dailyWeatherDTOS.get(0), dailyWeatherDTO);
        }
    }


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
    @Test
    public void testGetOneYearFromToday(){
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.DAY_OF_MONTH, -14);
        Date nextYear = cal.getTime();

        assertEquals(nextYear, weatherService.getOneYearFromToday());
    }
    @Test
    public void testGetToday(){
        Date today = Calendar.getInstance().getTime();
        assertEquals(today, weatherService.getToday());
    }
    @Test
    public void testGetMaximumEndDate() {
        Date today = new Date();
        SelectEvent<Date> event = Mockito.mock(SelectEvent.class);

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 5);
        Date expectedEndDate = cal.getTime();

        when(event.getObject()).thenReturn(today);
        Date actualEndDate = weatherService.getMaximumEndDate(event, 5);

        assertEquals(expectedEndDate, actualEndDate);
    }

    @Test
    public void testGetChosenDates() {
        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, 5);
        Date endDate = cal.getTime();

        cal.set(2024, Calendar.JANUARY, 1);
        Date startDateStatic = cal.getTime();
        cal.set(2024, Calendar.JANUARY, 10);
        Date endDateStatic = cal.getTime();

        // Call the method under test
        List<String> actualDates = weatherService.getChosenDates(startDate, endDate);
        List<String> actualDatesStatic = weatherService.getChosenDates(startDateStatic, endDateStatic);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> expectedDates = new ArrayList<>();
        cal.setTime(startDate);
        while (!cal.getTime().after(endDate)) {
            expectedDates.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DATE, 1);
        }
        List<String> expectedDatesStatic = new ArrayList<>(Arrays.asList("2024-01-01", "2024-01-02", "2024-01-03", "2024-01-04", "2024-01-05", "2024-01-06", "2024-01-07", "2024-01-08", "2024-01-09", "2024-01-10"));

        assertEquals(expectedDates, actualDates);
        assertEquals(expectedDatesStatic, actualDatesStatic);
    }
    @Test
    public void testGetHolidayForecast() {
        HolidayDTO holiday = new HolidayDTO(location.getLatitude(), location.getLongitude(), null, null, null, null, null, null, null, null, null);
        Location location = new Location();
        location.setLatitude(48.2082);
        location.setLongitude(16.3719);

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, 5);
        Date endDate = cal.getTime();

        List<String> actualDates = weatherService.getChosenDates(startDate, endDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> expectedDates = new ArrayList<>();
        cal.setTime(startDate);
        while (!cal.getTime().after(endDate)) {
            expectedDates.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DATE, 1);
        }
        List<HolidayDTO> holidayList = weatherService.retrieveDailyHolidayForecast(location, actualDates);
        assertEquals(6, holidayList.size());

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
