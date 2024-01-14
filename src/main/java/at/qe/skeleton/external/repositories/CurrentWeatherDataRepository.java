package at.qe.skeleton.external.repositories;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrentWeatherDataRepository extends JpaRepository<CurrentWeatherData, Long> {
    List<CurrentWeatherData> findByLocation_id(Long locationId);

    List<CurrentWeatherData> findByLocationOrderByAdditionTimeDesc(Location location);
}
