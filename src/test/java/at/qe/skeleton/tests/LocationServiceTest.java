package at.qe.skeleton.tests;


import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.LocationService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
public class LocationServiceTest {
    private static Logger LOGGER = LoggerFactory.getLogger(LocationServiceTest.class);
    @Autowired
    private LocationService locationService;

    @Test
    public void testAutocompleteInnsbruck() {
        String query = "Innsbruck";
        List<Location> locations = locationService.autocomplete(query);
        Location firstLocation = locations.get(0);
        assertEquals(firstLocation.getName(), "Innsbruck");
    }

    @Test
    public void testAutocompleteHall() {
        String query = "Hall";  // for simply "Hall" "Hallau" is expected.
        List<Location> locations = locationService.autocomplete(query);
        Location firstLocation = locations.get(0);
        assertEquals(firstLocation.getName(), "Hallau");
    }

    @Test
    public void testLoadDataFromJsonWithIOException() {
        // Provide a non-existing path to simulate an IOException
        String nonExistingPath = "non-existing-path";

        assertThrows(RuntimeException.class, () -> {
            locationService.loadDataFromJson(nonExistingPath);
        });
    }

    @Test
    public void testAutocompleteWithNonExistingLocation() {
        String query = "NonExistingLocation";
        List<Location> locations = locationService.autocomplete(query);
        assertTrue(locations.isEmpty(), "no locations should be found for a non-existing location");
    }

    @Test
    public void testLoadDataFromJsonWithEmptyPath() {
        // Provide an empty path
        String emptyPath = "";

        assertThrows(RuntimeException.class, () -> {
            locationService.loadDataFromJson(emptyPath);
        }, "loading data from an empty path should throw an exception");
    }

    @Test
    public void testGetFilePath() {
        String expectedPath = "src/main/resources/owm_city_list.json";      // if folder is moved, this has to be changed!!
        String actualPath = locationService.getFilePath();
        assertEquals(expectedPath, actualPath, "is the path setting with @Value correct.");
    }
}
