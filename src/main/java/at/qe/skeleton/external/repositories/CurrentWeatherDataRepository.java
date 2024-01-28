package at.qe.skeleton.external.repositories;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
/**
 * Repository interface for managing current weather data in the database.
 * Provides basic functionality to find the last x database entries for a specific location.
 */
public interface CurrentWeatherDataRepository extends JpaRepository<CurrentWeatherData, Long> {
    List<CurrentWeatherData> findByLocationId(Long locationId);

    List<CurrentWeatherData> findByLocationOrderByAdditionTimeDesc(Location location);

    @Query("SELECT c FROM CurrentWeatherData c WHERE c.location.name = :location ORDER BY c.additionTime DESC")
    List<CurrentWeatherData> findLatestByLocation(@Param("location") String location, Pageable pageable);
}
