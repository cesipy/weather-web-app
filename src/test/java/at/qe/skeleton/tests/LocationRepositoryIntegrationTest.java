package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.repositories.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class LocationRepositoryIntegrationTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testFindByNameStartingWithIgnoreCase() {
        String name = "Inns";

        Location location1 = new Location(1L, "Innsbruck", 47.2692,
                11.4041, "AT", "Austria", "6020");
        Location location2 = new Location(2L, "Inns Quay B", 47.2638,
                11.3946, "AT", "Austria", "6020");

        locationRepository.save(location1);
        locationRepository.save(location2);

        List<Location> locations = locationRepository.findByNameStartingWithIgnoreCase(name);

        assertEquals(2, locations.size());
    }

    @Test
    public void testSearch() {
        // Arrange
        String keyword = "Inns";
        Location location1 = new Location(1L, "Innsbruck", 47.2692, 11.4041, "AT", "Austria", "6020");
        Location location2 = new Location(2L, "Inns Quay B", 47.2638, 11.3946, "AT", "Austria", "6020");

        locationRepository.save(location1);
        locationRepository.save(location2);

        // Act
        List<Location> actualLocations = locationRepository.search(keyword);

        // Assert
        assertEquals(2, actualLocations.size());
        // Add more assertions based on your specific requirements
    }
}
