package at.qe.skeleton.tests;


import at.qe.skeleton.external.domain.DailyAggregationData;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.domain.TemperatureAggregationData;
import at.qe.skeleton.external.repositories.DailyAggregationDataRepository;
import at.qe.skeleton.external.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.external.repositories.HourlyWeatherDataRepository;
import at.qe.skeleton.external.repositories.TemperatureAggregationDataRepository;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.util.List;

@SpringBootTest
@WebAppConfiguration
public class WeatherRepositoryTest {
    @Autowired
    private HourlyWeatherDataRepository hourlyWeatherDataRepository;
    @Autowired
    private DailyWeatherDataRepository dailyWeatherDataRepository;
    @Autowired
    private DailyAggregationDataRepository dailyAggregationDataRepository;
    @Autowired
    private TemperatureAggregationDataRepository temperatureAggregationDataRepository;
    @Test
    public void hourlyWeatherTest(){
        HourlyWeatherData weatherData = new HourlyWeatherData(Instant.now(), 30.5, 30, 1, 50, 4, 1, 45, 150, 2, 3, 235, 30, null, null, Instant.now(), "Point Nemo");
        hourlyWeatherDataRepository.save(weatherData);

        List<HourlyWeatherData> records = hourlyWeatherDataRepository.findLatestByLocation("Point Nemo", PageRequest.of(0, 1));
        assertEquals(1, records.size(), "Was the record correctly saved?");

        hourlyWeatherDataRepository.delete(records.get(0));
        records = hourlyWeatherDataRepository.findLatestByLocation("Point Nemo", PageRequest.of(0, 1));
        assertEquals(0, records.size(), "Was the record correctly deleted?");
    }

    @Test
    public void dailyWeatherTest(){
        DailyAggregationData aggregationData = new DailyAggregationData(7, 15, 8, 4, 2, 22);
        TemperatureAggregationData temperatureData = new TemperatureAggregationData(7, 15, 4, 2);
        DailyWeatherData weatherData = new DailyWeatherData(Instant.now(), Instant.now(), Instant.now(), Instant.now(), Instant.now(), 10, "Total Eclipse", aggregationData, temperatureData, 1, 50, 4.3, 1.2, 45.7, 45, 2, 1, 235, null, null, Instant.now(), "Point Nemo");

        dailyAggregationDataRepository.save(aggregationData);
        temperatureAggregationDataRepository.save(temperatureData);
        dailyWeatherDataRepository.save(weatherData);
        List<DailyWeatherData> records = dailyWeatherDataRepository.findLatestByLocation("Point Nemo", PageRequest.of(0, 1));
        assertEquals(1, records.size(), "Was the record correctly saved?");

        dailyWeatherDataRepository.delete(records.get(0));
        records = dailyWeatherDataRepository.findLatestByLocation("Point Nemo", PageRequest.of(0, 1));
        assertEquals(0, records.size(), "Was the record correctly deleted?");
    }
}
