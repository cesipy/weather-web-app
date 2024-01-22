package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.controllers.SearchWeatherController;
import at.qe.skeleton.external.controllers.WeatherController;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.controllers.MessageService;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.internal.model.Userx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SearchWeatherControllerTest {

    @InjectMocks
    private SearchWeatherController searchWeatherController;
    @Mock
    private LocationService locationService;
    @Mock
    private WeatherController weatherController;
    @Mock
    private MessageService messageService;


    private Location mockLocation1;
    private Location mockLocation2;
    private Userx mockUser;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockLocation1 = new Location(1L, "Innsbruck", 47.0, 13.0, "AT",
                "Austria", "1111");
        mockLocation2 = new Location(2L, "Vienna", 40.0, 13.0, "AT",
                "Austria", "2222");


        doNothing().when(messageService).showWarnMessage(anyString());
        mockUser = new Userx();
    }

    @Test
    public void testIsLocationValid_LocationIsNull() throws EmptyLocationException, ApiQueryException, IOException {
        searchWeatherController.setLocationToSearch(null);
        assertFalse(searchWeatherController.isLocationValid());
    }

    @Test
    public void testIsLocationValid() throws EmptyLocationException, ApiQueryException {
        searchWeatherController.setLocationToSearch(mockLocation1.getName());

        when(locationService.retrieveLocation(mockLocation1.getName()))
                .thenReturn(mockLocation1);

        searchWeatherController.isLocationValid();

        assertEquals(mockLocation1, searchWeatherController.getCurrentLocation());
        assertTrue(searchWeatherController.isLocationValid());

    }

    @Test
    public void testIsLocationValid_singleLocationNull() throws EmptyLocationException, ApiQueryException {
        searchWeatherController.setLocationToSearch(mockLocation1.getName());
        assertEquals(mockLocation1.getName(), searchWeatherController.getLocationToSearch());

        when(locationService.retrieveLocation(mockLocation1.getName()))
                .thenReturn(null);

        searchWeatherController.isLocationValid();

        assertFalse(searchWeatherController.isLocationValid());
    }

    @Test
    public void testIsLocationValid_ApiQueryException() throws EmptyLocationException, ApiQueryException {
        searchWeatherController.setLocationToSearch(mockLocation1.getName());

        when(locationService.retrieveLocation(mockLocation1.getName()))
                .thenThrow(new ApiQueryException("Test API query exception"));

        assertFalse(searchWeatherController.isLocationValid());
        verify(messageService).showWarnMessage("An error occurred!");
    }

    @Test
    public void testIsLocationValid_EmptyLocationException() throws EmptyLocationException, ApiQueryException {
        searchWeatherController.setLocationToSearch(mockLocation1.getName());

        when(locationService.retrieveLocation(mockLocation1.getName()))
                .thenThrow(new EmptyLocationException("Test empty location exception"));

        assertFalse(searchWeatherController.isLocationValid());
        verify(messageService).showInfoMessage("Location 'Innsbruck' not found!");
    }

}
