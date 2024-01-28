package at.qe.skeleton.external.repositories;


import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.internal.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Repository interface for managing {@link DailyWeatherData} entities in the database.
 * Provides basic functionality to find the last x database entries for a specific location.
 */
public interface DailyWeatherDataRepository extends AbstractRepository<DailyWeatherData, Long> {

    @Query("SELECT d FROM DailyWeatherData d WHERE d.location = :location ORDER BY d.timestamp DESC")
    List<DailyWeatherData> findLatestByLocation(@Param("location") String location, Pageable pageable);

}
