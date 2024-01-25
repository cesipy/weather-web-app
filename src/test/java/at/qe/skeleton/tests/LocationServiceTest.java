package at.qe.skeleton.tests;


import at.qe.skeleton.external.controllers.EmptyLocationException;
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
    public void testAutocompleteInnsbruck() throws EmptyLocationException {
        String query = "Innsbruck";
        List<Location> locations = locationService.autocomplete(query);
        Location firstLocation = locations.get(0);
        assertEquals("Innsbruck", firstLocation.getName());
    }

    @Test
    public void testAutocompleteHall() throws EmptyLocationException {

        String query = "Hall";  // for simply "Hall" "Hall in Tirol" is expected.
        List<Location> locations = locationService.autocomplete(query);
        Location firstLocation = locations.get(0);
        assertEquals("Hall in Tirol", firstLocation.getName());
    }


    @Test
    public void testAutocompleteWithWhitespaceName() {
        // Arrange
        String whitespaceName = "   ";

        // Act and Assert
        EmptyLocationException exception = assertThrows(EmptyLocationException.class, () -> {
            locationService.autocomplete(whitespaceName);
        });

        assertEquals("The given location:     is empty!", exception.getMessage());
    }


    @Test
    public void testLoadDataFromJsonWithIOException() {
        String nonExistingPath = "non-existing-path";

        assertThrows(RuntimeException.class, () -> {
            locationService.loadDataFromJson(nonExistingPath);
        });
    }

    @Test
    public void testAutocompleteWithNonExistingLocation() throws EmptyLocationException {
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

    @Test
    public void testGetAllLocations() {
        // Assuming you have a known number of locations in your test environment
        int expectedSize = 74048;  // Replace this with the expected size

        List<Location> allLocations = locationService.getAllLocations();

        assertNotNull(allLocations, "List of locations should not be null");
        assertEquals(expectedSize, allLocations.size(), "List size should match the expected size");
    }

    @Test
    public void testRetrieveLocationByExactName() {
        // Arrange
        String locationName = "Innsbruck";

        // Act
        Location result = locationService.retrieveLocationByExactName(locationName);

        // Assert
        assertNotNull(result, "Location should not be null");
        assertEquals(locationName, result.getName(), "Location name should match");
    }

    @Test
    public void testRetrieveLocationByExactNameNotFound() {
        // Arrange
        String locationName = "NonExistentLocation";

        // Act
        Location result = locationService.retrieveLocationByExactName(locationName);

        // Assert
        assertNull(result, "Location should be null for a non-existent location");
    }
}
