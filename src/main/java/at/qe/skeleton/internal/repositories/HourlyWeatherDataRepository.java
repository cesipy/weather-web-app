package at.qe.skeleton.internal.repositories;


import at.qe.skeleton.external.domain.HourlyWeatherData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
/**
 * Repository interface for managing {@link HourlyWeatherData} entities in the database.
 * Provides basic functionality to find the last x database entries for a specific location.
 */
public interface HourlyWeatherDataRepository extends AbstractRepository<HourlyWeatherData, Long> {
    @Query("SELECT d FROM HourlyWeatherData d WHERE d.location = :location ORDER BY d.timestamp DESC")
    List<HourlyWeatherData> findLatestByLocation(@Param("location") String location, Pageable pageable);
}
