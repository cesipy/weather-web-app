package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.services.LocationApiRequestService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherDataService;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Component
@Scope("view")
public class LocationApiDemoBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationApiDemoBean.class);

    @Autowired
    private LocationApiRequestService locationApiRequestService;
    @Autowired
    private DailyWeatherDataRepository dailyWeatherDataRepository;
    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    @Autowired
    private WeatherDataService weatherDataService;
    private LocationDTO currentLocation;
    private HourlyWeatherDTO currentWeather;
    private HourlyWeatherDTO weatherInOneHour;

    private DailyWeatherDTO dailyWeatherToday;
    private DailyWeatherDTO dailyWeatherTomorrow;
    private DailyWeatherDTO dailyWeatherDAT;
    private DailyWeatherDTO dailyWeatherInThreeDays;
    private String query_name;
    private final int LIMIT = 1;


    @PostConstruct
    public void init() {
        try {
            List<LocationDTO> answer = this.locationApiRequestService.retrieveLocations(getQuery_name(), getLIMIT());

            // Check if the list is not empty
            if (!answer.isEmpty()) {
                // only process first entry in List of LocationDTOs

                LocationDTO firstLocation = answer.get(0);
                Pageable topOne = PageRequest.of(0, 1);
                List<DailyWeatherData> latestData = dailyWeatherDataRepository.findLatestByLocation(firstLocation.name(), topOne);

                if (!latestData.isEmpty()) {
                    DailyWeatherData latestRecord = latestData.get(0);
                    Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);

                    if (latestRecord.getAdditionTime().isAfter(oneHourAgo)) {
                        LOGGER.warn("Record is younger than an hour");
                        return;
                    }
                }

                this.setLocation(firstLocation);

                CurrentAndForecastAnswerDTO forecastAnswer = this.weatherApiRequestService.retrieveCurrentAndForecastWeather(firstLocation.latitude(), firstLocation.longitude());
                List<HourlyWeatherDTO> hourlyWeatherList = forecastAnswer.hourlyWeather();

                this.setCurrentWeather(hourlyWeatherList.get(0));
                this.setWeatherInOneHour(hourlyWeatherList.get(1));

                List<DailyWeatherDTO> dailyWeatherList = forecastAnswer.dailyWeather();
                this.setDailyWeatherToday(dailyWeatherList.get(0));
                weatherDataService.saveDailyWeatherFromDTO(dailyWeatherList.get(0), firstLocation.name());
                this.setDailyWeatherTomorrow(dailyWeatherList.get(1));
                weatherDataService.saveDailyWeatherFromDTO(dailyWeatherList.get(1), firstLocation.name());
                this.setDailyWeatherDAT(dailyWeatherList.get(2));
                weatherDataService.saveDailyWeatherFromDTO(dailyWeatherList.get(2), firstLocation.name());
                this.setDailyWeatherInThreeDays(dailyWeatherList.get(3));
                weatherDataService.saveDailyWeatherFromDTO(dailyWeatherList.get(3), firstLocation.name());

                //LOGGER.info("current location: " + currentLocation);
            } else {
                LOGGER.warn("The list of locations is empty.");
            }
        } catch (Exception e) {
            LOGGER.error("error in request in locationApi", e);
            throw new RuntimeException(e);
        }
    }



    public String getQuery_name() {
        return query_name;
    }

    public void setQuery_name(String query_name) {
        this.query_name = query_name;
    }

    public int getLIMIT() {
        return LIMIT;
    }

    public void setLocation(LocationDTO location) {
        this.currentLocation = location;
    }

    public LocationDTO getCurrentLocation() {
        return currentLocation;
    }

    public HourlyWeatherDTO getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(HourlyWeatherDTO currentWeather) {
        this.currentWeather = currentWeather;
    }

    public HourlyWeatherDTO getWeatherInOneHour() {
        return weatherInOneHour;
    }

    public void setWeatherInOneHour(HourlyWeatherDTO weatherInOneHour) {
        this.weatherInOneHour = weatherInOneHour;
    }

    public DailyWeatherDTO getDailyWeatherToday() {
        return dailyWeatherToday;
    }

    public void setDailyWeatherToday(DailyWeatherDTO dailyWeatherToday) {
        this.dailyWeatherToday = dailyWeatherToday;
    }

    public DailyWeatherDTO getDailyWeatherTomorrow() {
        return dailyWeatherTomorrow;
    }

    public void setDailyWeatherTomorrow(DailyWeatherDTO dailyWeatherTomorrow) {
        this.dailyWeatherTomorrow = dailyWeatherTomorrow;
    }

    public DailyWeatherDTO getDailyWeatherDAT() {
        return dailyWeatherDAT;
    }

    public DailyWeatherDTO getDailyWeatherInThreeDays() {
        return dailyWeatherInThreeDays;
    }

    public void setDailyWeatherInThreeDays(DailyWeatherDTO dailyWeatherInThreeDays) {
        this.dailyWeatherInThreeDays = dailyWeatherInThreeDays;
    }

    public void setDailyWeatherDAT(DailyWeatherDTO dailyWeatherDAT) {
        this.dailyWeatherDAT = dailyWeatherDAT;
    }

    public List<HourlyWeatherDTO> getCurrentWeatherAsList() {
        if (currentWeather != null) {
            return Collections.singletonList(currentWeather);
        } else {
            return Collections.emptyList();
        }
    }


}

