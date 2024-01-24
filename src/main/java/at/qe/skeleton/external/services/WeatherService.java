package at.qe.skeleton.external.services;

import at.qe.skeleton.external.controllers.CurrentlyHourlyDailyWeather;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.CurrentWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyTemperatureAggregationDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HolidayDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HumidityDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.PrecipitationDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.HourlyWeatherDataRepository;
import org.primefaces.event.SelectEvent;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

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

    private Location location;
    private HourlyWeatherDTO currentWeather;
    private HourlyWeatherDTO weatherInOneHour;
    private List<HourlyWeatherDTO> hourlyWeatherList;
    private List<DailyWeatherDTO> dailyWeatherList;


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
    private CurrentlyHourlyDailyWeather fetchCurrentWeatherAndForecast(Location location) throws ApiQueryException {
        Pageable lastEightEntries = PageRequest.of(0, 8);
        Pageable lastFortyEightEntries = PageRequest.of(0, 2);

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
            this.setHourlyWeatherList(currentAndForecastAnswerDTO.hourlyWeather());
            for(int n = 0; n < getHourlyWeatherList().size(); n++){
                weatherDataService.saveHourlyWeatherFromDTO(hourlyWeatherList.get(n), location.getName());
            }

            this.setDailyWeatherList(currentAndForecastAnswerDTO.dailyWeather());
            for(int n = 0; n < 8; n++){
                weatherDataService.saveDailyWeatherFromDTO(getDailyWeatherList().get(n), location.getName());
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

    public Date getOneYearFromToday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.DAY_OF_MONTH, -14);
        return cal.getTime();
    }

    public Date getToday(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public Date getMaximumEndDate(SelectEvent event, int days){
        Date startDate = (Date) event.getObject();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public List<String> getChosenDates(Date start_date, Date end_date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar start = Calendar.getInstance();
        start.setTime(start_date);
        Calendar end = Calendar.getInstance();
        end.setTime(end_date);

        List<String> chosenDates = new ArrayList<>();
        while (!start.after(end)) {
        chosenDates.add(sdf.format(start.getTime()));
        start.add(Calendar.DATE, 1);
        }

        return chosenDates;

    }
    public List<HolidayDTO> retrieveDailyHolidayForecast(Location location, List<String> chosenDates){
        List<HolidayDTO> holidays = new ArrayList<>();
        try{
            for (String i : chosenDates) {
                HolidayDTO holiday = weatherApiRequestService.retrieveDailyHolidayForecast(location.getLatitude(), location.getLongitude(), i);
                holidays.add(holiday);
            }
        }catch (Exception e) {
            logger.error("error in request in WeatherApi when retrieving Holiday Data", e);
            throw new RuntimeException(e);
        }
        return holidays;
    }

    public HolidayDTO getPastAverageDTO(Date start_date, Date end_date, Location location){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        long diffInMillies = Math.abs(end_date.getTime() - start_date.getTime());

        long middlePoint = diffInMillies / 2;

        Date middleDate = new Date(start_date.getTime() + middlePoint);

        List<String> pastFiveYears = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            cal.setTime(middleDate);
            cal.add(Calendar.YEAR, -i);
            Date pastYearDate = cal.getTime();
            pastFiveYears.add(sdf.format(pastYearDate));
        }

        List<HolidayDTO> pastAverage = new ArrayList<>();
        for(String i : pastFiveYears){
            HolidayDTO holiday = weatherApiRequestService.retrieveDailyHolidayForecast(location.getLatitude(), location.getLongitude(), i);
            pastAverage.add(holiday);
        }
        return convertToAverageDTO(pastAverage, location, middleDate);
    }

    public HolidayDTO convertToAverageDTO(List<HolidayDTO> pastFiveYears, Location location, Date middleDate){
        double sumMinTemp = 0.0;
        double sumMaxTemp = 0.0;
        double sumMorningTemp = 0.0;
        double sumAfternoonTemp = 0.0;
        double sumEveningTemp = 0.0;
        double sumNightTemp = 0.0;
        int sumPrecipitation = 0;
        int sumHumidity = 0;

        for (HolidayDTO holiday : pastFiveYears){
            sumMinTemp += holiday.temperatureDTO().minimumDailyTemperature();
            sumMaxTemp += holiday.temperatureDTO().maximumDailyTemperature();
            sumMorningTemp += holiday.temperatureDTO().morningTemperature();
            sumAfternoonTemp += holiday.temperatureDTO().dayTemperature();
            sumEveningTemp += holiday.temperatureDTO().eveningTemperature();
            sumNightTemp += holiday.temperatureDTO().nightTemperature();
            sumPrecipitation += holiday.precipitationDTO().total();
            sumHumidity += holiday.humidityDTO().afternoon();
        }
        sumMinTemp = Math.round(sumMinTemp / pastFiveYears.size() * 100.0 / 100.0);
        sumMaxTemp = Math.round(sumMaxTemp / pastFiveYears.size() * 100.0 / 100.0);
        sumMorningTemp = Math.round(sumMorningTemp / pastFiveYears.size() * 100.0 / 100.0);
        sumAfternoonTemp = Math.round(sumAfternoonTemp / pastFiveYears.size() * 100.0 / 100.0);
        sumEveningTemp = Math.round(sumEveningTemp / pastFiveYears.size() * 100.0 / 100.0);
        sumNightTemp = Math.round(sumNightTemp / pastFiveYears.size() * 100.0 / 100.0);
        sumPrecipitation = sumPrecipitation / pastFiveYears.size();
        sumHumidity = sumHumidity / pastFiveYears.size();

        HolidayDTO pastAvg = new HolidayDTO(
                location.getLatitude(),
                location.getLongitude(),
                null,
                middleDate,
                null,
                null,
                new HumidityDTO(
                        sumHumidity
                ),
                new PrecipitationDTO(
                        sumPrecipitation
                ),
                null,
                new DailyTemperatureAggregationDTO(
                        sumMorningTemp,
                        sumAfternoonTemp,
                        sumEveningTemp,
                        sumNightTemp,
                        sumMinTemp,
                        sumMaxTemp
                ),
                null
        );
        return pastAvg;
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
