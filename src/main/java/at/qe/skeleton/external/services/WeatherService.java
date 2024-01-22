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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


/**
 * Service class for processing weather information.
 * This service handles the retrieval and storage of current and forecast weather data.
 * It checks the freshness of the data and decides whether to fetch new data from an API or use the cached data.
 *
 * The class includes methods for processing weather data for a specific location,
 * fetching current weather data, checking data staleness, and retrieving data from the API.
 */
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


    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    /**
     * Processes weather information for the specified location.
     *
     * Calls the {@link #fetchCurrentWeatherAndForecast(Location)} method to retrieve
     * and process current and forecast weather data for the given location.
     *
     * @param location The location for which weather information is to be processed.
     * @return A {@link CurrentlyHourlyDailyWeather} containing hourly and daily weather information.
     * @throws ApiQueryException If there is an issue querying the weather API.
     */
    public CurrentlyHourlyDailyWeather processWeatherForLocation(Location location) throws ApiQueryException {

        return fetchCurrentWeatherAndForecast(location);
    }

    /**
     * Fetches current weather and forecast for the specified location.
     *
     * The method first checks if the cached weather data is stale. If stale, it retrieves the latest data from the database.
     * If not stale, it fetches new data from the weather API, updates the database, and returns the result.
     *
     * @param location The location for which weather data is to be fetched.
     * @return A {@link CurrentlyHourlyDailyWeather} containing hourly and daily weather information.
     * @throws ApiQueryException If there is an issue querying the weather API.
     */
    public CurrentlyHourlyDailyWeather fetchCurrentWeatherAndForecast(Location location) throws ApiQueryException {
        Pageable lastEightEntries = PageRequest.of(0, 8);
        Pageable lastFortyEightEntries = PageRequest.of(0, 2);
        Pageable lastEntry = PageRequest.of(0, 1);

        List<DailyWeatherData> latestData = dailyWeatherDataRepository
                .findLatestByLocation(location.getName(), lastEightEntries);
        List<HourlyWeatherData> latestDataHourly = hourlyWeatherDataRepository
                .findLatestByLocation(location.getName(), lastFortyEightEntries);

        if (isWeatherDataStale(latestData, latestDataHourly)) {
            logger.info("taking weather data for {} from database", location.getName());
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
            List<HourlyWeatherDTO> hourlyWeatherList = currentAndForecastAnswerDTO.hourlyWeather();

            for (HourlyWeatherDTO hourlyWeatherDTO : hourlyWeatherList) {
                weatherDataService.saveHourlyWeatherFromDTO(hourlyWeatherDTO, location.getName());
            }

            List<DailyWeatherDTO> dailyWeatherList = currentAndForecastAnswerDTO.dailyWeather();
            for (DailyWeatherDTO dailyWeatherDTO : dailyWeatherList) {
                weatherDataService.saveDailyWeatherFromDTO(dailyWeatherDTO, location.getName());
            }
            return new CurrentlyHourlyDailyWeather(currentAndForecastAnswerDTO.hourlyWeather(), currentAndForecastAnswerDTO.dailyWeather());
        }
    }

    /**
     * Retrieves current and forecast weather data from the weather API for the specified location.
     *
     * @param location The location for which weather data is to be retrieved.
     * @return A {@link CurrentAndForecastAnswerDTO} containing current and forecast weather information.
     * @throws ApiQueryException If there is an issue querying the weather API.
     */
    private CurrentAndForecastAnswerDTO retrieveWeatherDataApi(Location location) throws ApiQueryException {

        return weatherApiRequestService
                .retrieveCurrentAndForecastWeather(location.getLatitude(), location.getLongitude());
    }


    /**
     * Checks if the provided weather data is considered stale based on the time elapsed since its addition.
     *
     * @param dailyWeatherDataList  The list of daily weather data entries.
     * @param hourlyWeatherDataList The list of hourly weather data entries.
     * @return {@code true} if the weather data is considered stale, {@code false} otherwise.
     */
    private boolean isWeatherDataStale(List<DailyWeatherData> dailyWeatherDataList,
                                        List<HourlyWeatherData> hourlyWeatherDataList) {
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);

        if (!dailyWeatherDataList.isEmpty()
                && !hourlyWeatherDataList.isEmpty()) {

            DailyWeatherData latestDailyWeatherData = dailyWeatherDataList.get(0);
            HourlyWeatherData latestHourlyWeatherData = hourlyWeatherDataList.get(0);


            return (latestDailyWeatherData.getAdditionTime().isAfter(oneHourAgo)
                    && latestHourlyWeatherData.getAdditionTime().isAfter(oneHourAgo));
        }
        return false;
    }

    /**
     * Fetches current weather data for the specified location.
     *
     * The method first checks if the cached current weather data is stale. If stale, it retrieves the latest data from the database.
     * If not stale, it fetches new data from the weather API, updates the database, and returns the result.
     *
     * @param location The location for which current weather data is to be fetched.
     * @return A {@link CurrentWeatherData} containing current weather information.
     * @throws ApiQueryException If there is an issue querying the weather API.
     */
    public CurrentWeatherData fetchCurrentWeather(Location location) throws ApiQueryException {
        List<CurrentWeatherData> currentWeatherDataList = currentWeatherDataRepository
                .findByLocationOrderByAdditionTimeDesc(location);

        // is data outdated or no data is saved?
        if (currentWeatherDataList.isEmpty() || !isCurrentWeatherDataStale(currentWeatherDataList.get(0))) {
            logger.info("fetching weather data from api: {}", location.getName());
            CurrentAndForecastAnswerDTO weather = weatherApiRequestService
                    .retrieveCurrentAndForecastWeather(location.getLatitude(), location.getLongitude());

            weatherDataService.saveCurrentWeatherFromDTO(weather.currentWeather(), location);

            return currentWeatherDataRepository
                    .findByLocationOrderByAdditionTimeDesc(location).get(0);
        } else {
            logger.info("Taking weather data from database for location {}", location);
            return currentWeatherDataList.get(0);
        }
    }


    /**
     * Checks if the provided weather data is considered stale based on the time elapsed since its addition.
     *
     * @param weatherData The weatherData to check for staleness.
     * @return {@code true} if the weather data is considered stale, {@code false} otherwise.
     */
    private boolean isCurrentWeatherDataStale(CurrentWeatherData weatherData) {
        Instant tenMinutesAgo = Instant.now().minus(10, ChronoUnit.MINUTES);
        Instant additionTime = weatherData.getAdditionTime();

        return additionTime.isAfter(tenMinutesAgo);
    }


}
