package at.qe.skeleton.tests;


import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.controllers.LocationControllerDb;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocationControllerDbTest {

    @InjectMocks
    private LocationControllerDb locationControllerDb;

    @Mock
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAutocomplete_Successful() throws EmptyLocationException {
        String query = "Vienna";
        List<Location> expectedLocations = List.of(new Location(1L, "Vienna",
                48.8566, 2.3522, "AT", "Austria", "12345"));
        when(locationService.autocomplete(query)).thenReturn(expectedLocations);

        List<Location> resultLocations = locationControllerDb.autocomplete(query);

        assertEquals(expectedLocations, resultLocations);
        verify(locationService).autocomplete(query);
    }

    @Test
    void testAutocomplete_EmptyResult() throws EmptyLocationException {
        String query = "NonExistent";
        when(locationService.autocomplete(query)).thenReturn(Collections.emptyList());

        List<Location> resultLocations = locationControllerDb.autocomplete(query);

        assertTrue(resultLocations.isEmpty());
        verify(locationService).autocomplete(query);
    }

    @Test
    void testAutocomplete_EmptyLocationException() throws EmptyLocationException {

        String query = "InvalidQuery";
        doThrow(new EmptyLocationException()).when(locationService).autocomplete(query);

        List<Location> resultLocations = locationControllerDb.autocomplete(query);


        assertEquals(resultLocations, Collections.emptyList());

    }

    @Test
    void testRequestFirstMatch_Successful() throws EmptyLocationException {

        String locationName = "Vienna";
        locationControllerDb.setLocationName(locationName);
        Location expectedLocation = new Location(1L, "Vienna", 48.8566, 2.3522,
                "AT", "Austria", "12345");
        when(locationService.autocomplete(locationName)).thenReturn(List.of(expectedLocation));

        Location resultLocation = locationControllerDb.requestFirstMatch();

        assertEquals(expectedLocation, resultLocation);
        assertEquals(expectedLocation.toDebugString(), resultLocation.toDebugString());
        assertEquals(expectedLocation, locationControllerDb.getCurrentLocation());
        verify(locationService).autocomplete("Vienna");
        assertEquals("", locationControllerDb.getLocationName());
    }

    @Test
    void testRequestFirstMatch_NoMatch() throws EmptyLocationException {
        String locationName = "qwertzu";
        locationControllerDb.setLocationName(locationName);
        when(locationService.autocomplete(locationName)).thenReturn(Collections.emptyList());


        Location resultLocation = locationControllerDb.requestFirstMatch();

        assertNull(resultLocation);
        assertNull(locationControllerDb.getCurrentLocation());
    }

    @Test
    void testRequestFirstMatch_EmptyLocationException() throws EmptyLocationException {
        String invalidQuery = "asdfasdf";
        when(locationService.autocomplete(invalidQuery)).thenThrow(new EmptyLocationException());

        Location resultLocation = locationControllerDb.requestFirstMatch();

        assertNull(resultLocation);
        assertNull(locationControllerDb.getCurrentLocation());
        assertNull(locationControllerDb.getLocationName());
    }

    @Test
    void testRequestFirstMatch_GenericException() throws EmptyLocationException {
        when(locationService.autocomplete("ErrorQuery")).thenThrow(new RuntimeException("An unexpected error"));

        Location resultLocation = locationControllerDb.requestFirstMatch();

        assertNull(resultLocation);
        assertNull(locationControllerDb.getCurrentLocation());
        assertNull(locationControllerDb.getLocationName());

    }

}
