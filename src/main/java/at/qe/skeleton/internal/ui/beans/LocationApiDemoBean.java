package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.location.LocationDTO;
import at.qe.skeleton.external.services.LocationApiRequestService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherDataService;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.HourlyWeatherDataRepository;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    private HourlyWeatherDataRepository hourlyWeatherDataRepository;
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
        if(getQuery_name() != null){
            String location = getQuery_name();
            setQuery_name(location.replace(" ", "_"));
        }
        if(getQuery_name()!= null) {
            try {

                List<LocationDTO> answer = this.locationApiRequestService.retrieveLocations(getQuery_name(), getLIMIT());

                // Check if the list is not empty
                if (!answer.isEmpty()) {
                    // only process first entry in List of LocationDTOs

                    LocationDTO firstLocation = answer.get(0);
                    Pageable last_four_entries = PageRequest.of(0, 4);
                    Pageable last_two_entries = PageRequest.of(0, 2);
                    List<DailyWeatherData> latestData = dailyWeatherDataRepository.findLatestByLocation(firstLocation.name(), last_four_entries);
                    List<HourlyWeatherData> latestDataHourly = hourlyWeatherDataRepository.findLatestByLocation(firstLocation.name(), last_two_entries);

                    if (!latestData.isEmpty() && !latestDataHourly.isEmpty()) {
                        DailyWeatherData latestRecord = latestData.get(0);
                        HourlyWeatherData latestRecordHourly = latestDataHourly.get(0);
                        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);

                        if (latestRecord.getAdditionTime().isAfter(oneHourAgo) && latestRecordHourly.getAdditionTime().isAfter(oneHourAgo)) {
                            this.setCurrentWeather(weatherDataService.convertHourlyDataToDTO(latestDataHourly.get(0)));
                            this.setWeatherInOneHour(weatherDataService.convertHourlyDataToDTO(latestDataHourly.get(1)));

                            this.setDailyWeatherToday(weatherDataService.convertDailyDataToDTO(latestData.get(0)));
                            this.setDailyWeatherTomorrow(weatherDataService.convertDailyDataToDTO(latestData.get(1)));
                            this.setDailyWeatherDAT(weatherDataService.convertDailyDataToDTO(latestData.get(2)));
                            this.setDailyWeatherInThreeDays(weatherDataService.convertDailyDataToDTO(latestData.get(3)));
                            return;
                        }
                    }
                    LOGGER.info(firstLocation.name());
                    LOGGER.info(firstLocation.latitude() + " " + firstLocation.longitude());
                    this.setLocation(firstLocation);

                    CurrentAndForecastAnswerDTO forecastAnswer = this.weatherApiRequestService.retrieveCurrentAndForecastWeather(firstLocation.latitude(), firstLocation.longitude());
                    List<HourlyWeatherDTO> hourlyWeatherList = forecastAnswer.hourlyWeather();

                    this.setCurrentWeather(hourlyWeatherList.get(0));
                    weatherDataService.saveHourlyWeatherFromDTO(hourlyWeatherList.get(0), firstLocation.name());
                    this.setWeatherInOneHour(hourlyWeatherList.get(1));
                    weatherDataService.saveHourlyWeatherFromDTO(hourlyWeatherList.get(1), firstLocation.name());

                    List<DailyWeatherDTO> dailyWeatherList = forecastAnswer.dailyWeather();
                    this.setDailyWeatherToday(dailyWeatherList.get(0));
                    weatherDataService.saveDailyWeatherFromDTO(dailyWeatherList.get(0), firstLocation.name());
                    this.setDailyWeatherTomorrow(dailyWeatherList.get(1));
                    weatherDataService.saveDailyWeatherFromDTO(dailyWeatherList.get(1), firstLocation.name());
                    this.setDailyWeatherDAT(dailyWeatherList.get(2));
                    weatherDataService.saveDailyWeatherFromDTO(dailyWeatherList.get(2), firstLocation.name());
                    this.setDailyWeatherInThreeDays(dailyWeatherList.get(3));
                    weatherDataService.saveDailyWeatherFromDTO(dailyWeatherList.get(3), firstLocation.name());


                } else {
                    LOGGER.warn("The list of locations is empty.");
                }
            } catch (Exception e) {
                LOGGER.error("error in request in locationApi", e);
                throw new RuntimeException(e);
            }
        }
    }



    public String getQuery_name() {
        return query_name;
    }

    @RequestMapping(value = "/secured/detail.xhtml", method = RequestMethod.GET)
    public void setQuery_name(@RequestParam("location") String location) {
        this.query_name = location;
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

    public List<HourlyWeatherDTO> getWeatherInOneHourAsList() {
        if (weatherInOneHour != null) {
            return Collections.singletonList(weatherInOneHour);
        } else {
            return Collections.emptyList();
        }
    }

    public List<DailyWeatherDTO> getdailyWeatherTodayAsList() {
        if (dailyWeatherToday != null) {
            return Collections.singletonList(dailyWeatherToday);
        } else {
            return Collections.emptyList();
        }
    }
    public List<DailyWeatherDTO> getdailyWeatherTomorrowAsList() {
        if (dailyWeatherTomorrow != null) {
            return Collections.singletonList(dailyWeatherTomorrow);
        } else {
            return Collections.emptyList();
        }
    }
    public List<DailyWeatherDTO> getdailyWeatherDATAsList() {
        if (dailyWeatherDAT != null) {
            return Collections.singletonList(dailyWeatherDAT);
        } else {
            return Collections.emptyList();
        }
    }
    public List<DailyWeatherDTO> getdailyWeatherInThreeDaysAsList() {
        if (dailyWeatherInThreeDays != null) {
            return Collections.singletonList(dailyWeatherInThreeDays);
        } else {
            return Collections.emptyList();
        }
    }
}

