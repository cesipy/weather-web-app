package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.internal.ui.beans.WeatherApiDemoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class WeatherApiDemoBeanTest {

    @InjectMocks
    WeatherApiDemoBean weatherApiDemoBean;

    @Mock
    WeatherApiRequestService weatherApiRequestService;

    @Mock
    LocationService locationService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInit() {
        // Set up test data
        List<Location> locations = new ArrayList<>();
        Location location = new Location();
        locations.add(location);

        // Mock behaviors
        when(locationService.getAllLocations()).thenReturn(locations);

        // Call the method under test
        weatherApiDemoBean.init();

        // Verify behaviors
        verify(locationService).getAllLocations();
    }

    @Test
    public void testGetLocations() {
        // Set up test data
        List<Location> locations = new ArrayList<>();
        Location location = new Location();
        locations.add(location);
        weatherApiDemoBean.setLocations(locations);

        // Call the method under test and assert the result
        assertEquals(locations, weatherApiDemoBean.getLocations());
    }
}

