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
        List<Location> locations = new ArrayList<>();
        Location location = new Location();
        locations.add(location);

        when(locationService.getAllLocations()).thenReturn(locations);

        weatherApiDemoBean.init();

        assertEquals(locations, weatherApiDemoBean.getLocations());
    }

    @Test
    public void testGetLocations() {
        List<Location> locations = new ArrayList<>();
        Location location = new Location();
        locations.add(location);
        weatherApiDemoBean.setLocations(locations);

        assertEquals(locations, weatherApiDemoBean.getLocations());
    }
}

