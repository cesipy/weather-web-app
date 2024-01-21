package at.qe.skeleton.external.services;

import at.qe.skeleton.external.controllers.CurrentlyHourlyDailyWeather;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.HourlyWeatherDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("application")
public class WeatherService {
    @Autowired
    private DailyWeatherDataRepository dailyWeatherDataRepository;
    @Autowired
    HourlyWeatherDataRepository hourlyWeatherDataRepository;
    @Autowired
    private WeatherApiRequestService weatherApiRequestService;
    @Autowired
    private WeatherDataService weatherDataService;
    @Autowired
    private CurrentWeatherDataRepository currentWeatherDataRepository;


    private Location location;
    private HourlyWeatherDTO currentWeather;
    private HourlyWeatherDTO weatherInOneHour;
    private List<HourlyWeatherDTO> hourlyWeatherList;
    private List<DailyWeatherDTO> dailyWeatherList;
    private List<CurrentWeatherDTO> currentWeatherDTOList;


    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    public CurrentlyHourlyDailyWeather processWeatherForLocation(Location location) throws ApiQueryException {
        logger.info("processing weather for {}", location);
        CurrentlyHourlyDailyWeather currentlyHourlyDailyWeather = fetchCurrentWeatherAndForecast(location);
        logger.info("processed info!");

        return currentlyHourlyDailyWeather;
    }

    private CurrentlyHourlyDailyWeather fetchCurrentWeatherAndForecast(Location location) throws ApiQueryException {
        Pageable lastEightEntries = PageRequest.of(0, 8);
        Pageable lastFortyEightEntries = PageRequest.of(0, 2);
        Pageable lastEntry = PageRequest.of(0, 1);

        List<DailyWeatherData> latestData = dailyWeatherDataRepository
                .findLatestByLocation(location.getName(), lastEightEntries);
        List<HourlyWeatherData> latestDataHourly = hourlyWeatherDataRepository
                .findLatestByLocation(location.getName(), lastFortyEightEntries);
        //List<CurrentWeatherData> latestDataCurrently = currentWeatherDataRepository
        //        .findLatestByLocation(location.getName(), lastEntry);

        if (isWeatherDataStale(latestData, latestDataHourly)) {
            // this.setLocation(location);         // temp

            ArrayList<HourlyWeatherDTO> latestHourlyWeather = new ArrayList<>();
            for(int n = latestDataHourly.size() - 1; n >= 0 ; n--){
                latestHourlyWeather.add(weatherDataService.convertHourlyDataToDTO(latestDataHourly.get(n)));
            }


            ArrayList<DailyWeatherDTO> latestWeather = new ArrayList<>();
            for(int n = latestData.size() - 1; n >= 0 ; n--){
                latestWeather.add(weatherDataService.convertDailyDataToDTO(latestData.get(n)));
            }

            return new CurrentlyHourlyDailyWeather(latestHourlyWeather, latestWeather);
        }
        else {
            // get new data from api
            CurrentAndForecastAnswerDTO currentAndForecastAnswerDTO = retrieveWeatherDataApi(location);

            logger.info("retrieve new data");
            this.setHourlyWeatherList(currentAndForecastAnswerDTO.hourlyWeather());
            for(int n = 0; n < getHourlyWeatherList().size(); n++){
                weatherDataService.saveHourlyWeatherFromDTO(hourlyWeatherList.get(n), location.getName());
            }

            this.setDailyWeatherList(currentAndForecastAnswerDTO.dailyWeather());
            for(int n = 0; n < getDailyWeatherList().size(); n++){
                weatherDataService.saveDailyWeatherFromDTO(getDailyWeatherList().get(n), location.getName());
            }
            return new CurrentlyHourlyDailyWeather(currentAndForecastAnswerDTO.hourlyWeather(), currentAndForecastAnswerDTO.dailyWeather());
        }
    }

    private CurrentAndForecastAnswerDTO retrieveWeatherDataApi(Location location) throws ApiQueryException {

        return weatherApiRequestService
                .retrieveCurrentAndForecastWeather(location.getLatitude(), location.getLongitude());
    }

    private boolean isWeatherDataStale(//List<CurrentWeatherData> currentWeatherDataList,
                                        List<DailyWeatherData> dailyWeatherDataList,
                                        List<HourlyWeatherData> hourlyWeatherDataList) {
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);

        if (!dailyWeatherDataList.isEmpty()
                && !hourlyWeatherDataList.isEmpty()
                //&& !currentWeatherDataList.isEmpty())
        ){

            DailyWeatherData latestDailyWeatherData = dailyWeatherDataList.get(0);
            HourlyWeatherData latestHourlyWeatherData = hourlyWeatherDataList.get(0);
            //CurrentWeatherData latestCurrentWeatherData =

            return (latestDailyWeatherData.getAdditionTime().isAfter(oneHourAgo)
                    && latestHourlyWeatherData.getAdditionTime().isAfter(oneHourAgo));
        }

        return false;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public List<HourlyWeatherDTO> getHourlyWeatherList() {
        return hourlyWeatherList;
    }

    public void setHourlyWeatherList(List<HourlyWeatherDTO> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }

    public List<DailyWeatherDTO> getDailyWeatherList() {
        return dailyWeatherList;
    }

    public void setDailyWeatherList(List<DailyWeatherDTO> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }
}
