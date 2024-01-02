package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.external.domain.DailyWeatherData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DailyWeatherDataRepository extends AbstractRepository<DailyWeatherData, Long> {

    @Query("SELECT d FROM DailyWeatherData d WHERE d.location = :location ORDER BY d.additionTime DESC")
    List<DailyWeatherData> findLatestByLocation(@Param("location") String location, Pageable pageable);

}
