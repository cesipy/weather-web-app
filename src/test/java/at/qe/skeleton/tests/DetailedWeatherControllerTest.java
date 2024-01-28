package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.DetailedWeatherController;
import at.qe.skeleton.external.services.MessageService;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.services.WeatherService;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DetailedWeatherControllerTest {
    @InjectMocks
    private DetailedWeatherController detailedWeatherController;

    @Mock
    private WeatherService weatherService;

    @Mock
    private LocationService locationService;

    @Mock
    private MessageService messageService;

    @Mock
    private ExternalContext externalContext;

    @Mock
    private FacesContext facesContext;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        doNothing().when(messageService).showWarnMessage(anyString());
        doNothing().when(messageService).showInfoMessage(anyString());

    }

    @AfterEach
    public void tearDown() {
        detailedWeatherController.setDailyWeatherList(null);
        detailedWeatherController.setHourlyWeatherList(null);
        detailedWeatherController.setDailyWeatherList(null);
    }

    @Test
    void testInit_LocationQueryNull() {
        when(externalContext.getRequestParameterMap()).thenReturn(Map.of("location", "Vienna"));
        when(facesContext.getExternalContext()).thenReturn(externalContext);

        detailedWeatherController.init();

        verify(messageService).showWarnMessage("An error occurred!");
        assertNull(detailedWeatherController.getHourlyWeatherList());
        assertNull(detailedWeatherController.getDailyWeatherList());
        assertNull(detailedWeatherController.getLocationQuery());
    }

    /*
    @Test
    void testInit_LocationQueryNotNull() throws EmptyLocationException, ApiQueryException {
        FacesContext facesContext = mock(FacesContext.class);
        ExternalContext externalContext = mock(ExternalContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);

        when(facesContext.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getRequestParameterMap()).thenReturn(Map.of("location", "Vienna"));

        assertNull(detailedWeatherController.getHourlyWeatherList());
        assertNull(detailedWeatherController.getDailyWeatherList());
        assertNull(detailedWeatherController.getLocationQuery());

        String queryName = "Innsbruck";
        Location mockLocation1 = new Location(1L, "Innsbruck", 47.0, 13.0, "AT",
                "Austria", "1111");
        CurrentlyHourlyDailyWeather expectedData = generateWeatherData();

        detailedWeatherController.setLocationQuery(queryName);


        when(locationService.retrieveLocation(queryName))
                .thenReturn(mockLocation1);
        when(weatherService.processWeatherForLocation(mockLocation1))
                .thenReturn(expectedData);


        detailedWeatherController.init();

        assertEquals(expectedData.getHourlyWeatherList(), detailedWeatherController.getHourlyWeatherList());
    }

    private CurrentlyHourlyDailyWeather generateWeatherData(){
        HourlyWeatherDTO hourlyWeatherDTO = new HourlyWeatherDTO(
                Instant.now(),
                10, 10,
                10, 10, 10, 10,
                10, 10, 10,
                10, 10, 1,
                null, null,
                null
        );

        DailyWeatherDTO dailyWeatherDTO = new DailyWeatherDTO(
                Instant.now(), Instant.now(),
                Instant.now(), Instant.now(),
                Instant.now(), 0, null, null, null,
                10, 10, 10, 10,
                10, 10, 10, 10, 0, null, null,
                null
        );

        return new CurrentlyHourlyDailyWeather(
                List.of(hourlyWeatherDTO), List.of(dailyWeatherDTO));
    }

     */
}
