package at.qe.skeleton.external.services;

import at.qe.skeleton.external.domain.DailyAggregationData;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.domain.TemperatureAggregationData;
import at.qe.skeleton.external.model.currentandforecast.misc.*;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.shared.WeatherDTO;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.internal.repositories.DailyAggregationDataRepository;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.HourlyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.TemperatureAggregationDataRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.time.Instant;

/**
 * Service class for managing and manipulating weather data.
 */
@Scope("application")
@Component
@Validated
public class WeatherDataService {

    @Autowired
    private DailyWeatherDataRepository dailyWeatherDataRepository;
    @Autowired
    private DailyAggregationDataRepository dailyAggregationDataRepository;
    @Autowired
    private TemperatureAggregationDataRepository temperatureAggregationDataRepository;
    @Autowired
    private HourlyWeatherDataRepository hourlyWeatherDataRepository;
    @Autowired
    private CurrentWeatherDataRepository currentWeatherDataRepository;

    /**
     * Converts a DailyWeatherDTO to a dailyWeatherData entity and saves it into the database.
     *
     * @param dailyWeatherDTO dailyWeather record, holding the weather data retrieved from the api.
     * @param location name of the location corresponding to the weather data.
     */
    public void saveDailyWeatherFromDTO(DailyWeatherDTO dailyWeatherDTO, String location) {
        DailyWeatherData dailyWeatherData = new DailyWeatherData();
        dailyWeatherData.setTimestamp(dailyWeatherDTO.timestamp());
        dailyWeatherData.setSunrise(dailyWeatherDTO.sunrise());
        dailyWeatherData.setSunset(dailyWeatherDTO.sunset());
        dailyWeatherData.setMoonrise(dailyWeatherDTO.moonrise());
        dailyWeatherData.setMoonset(dailyWeatherDTO.moonset());
        dailyWeatherData.setMoonPhase(dailyWeatherDTO.moonPhase());
        dailyWeatherData.setSummary(dailyWeatherDTO.summary());
        dailyWeatherData.setDailyTemperatureAggregation(getDailyAggregationFromDTO(dailyWeatherDTO.dailyTemperatureAggregation()));
        dailyWeatherData.setFeelsLikeTemperatureAggregation(getAggregationTemperatureFromDTO(dailyWeatherDTO.feelsLikeTemperatureAggregation()));
        dailyWeatherData.setPressure(dailyWeatherDTO.pressure());
        dailyWeatherData.setHumidity(dailyWeatherDTO.humidity());
        dailyWeatherData.setDewPoint(dailyWeatherDTO.dewPoint());
        dailyWeatherData.setWindSpeed(dailyWeatherDTO.windSpeed());
        dailyWeatherData.setWindGust(dailyWeatherDTO.windGust());
        dailyWeatherData.setWindDirection(dailyWeatherDTO.windDirection());
        dailyWeatherData.setClouds(dailyWeatherDTO.clouds());
        dailyWeatherData.setUvi(dailyWeatherDTO.uvi());
        dailyWeatherData.setProbabilityOfPrecipitation(dailyWeatherDTO.probabilityOfPrecipitation());
        dailyWeatherData.setRain(dailyWeatherDTO.rain());
        dailyWeatherData.setSnow(dailyWeatherDTO.snow());
        dailyWeatherData.setAdditionTime(Instant.now());
        dailyWeatherData.setLocation(location);

        dailyWeatherDataRepository.save(dailyWeatherData);
    }

    /**
     * Converts a dailyWeatherData entity to a DailyWeatherDTO record.
     *
     * @param data dailyWeatherData entity, holding the weather data retrieved from the database.
     * @return DailyWeatherDTO for the purpose of displaying the weather data on the website.
     */
    public DailyWeatherDTO convertDailyDataToDTO(DailyWeatherData data){
        return new DailyWeatherDTO(
                data.getTimestamp(),
                data.getSunrise(),
                data.getSunset(),
                data.getMoonrise(),
                data.getMoonset(),
                data.getMoonPhase(),
                data.getSummary(),
                converDailyAggregationToDTO(data.getDailyTemperatureAggregation()),
                convertTempAggregationToDTO(data.getFeelsLikeTemperatureAggregation()),
                data.getPressure(),
                data.getHumidity(),
                data.getDewPoint(),
                data.getWindSpeed(),
                data.getWindGust(),
                data.getWindDirection(),
                data.getClouds(),
                data.getUvi(),
                data.getProbabilityOfPrecipitation(),
                data.getRain(),
                data.getSnow(),
                null
        );
    }
    /**
     * Converts a DailyTemperatureAggregationDTO to a DailyAggregationData entity and saves it into the database.
     *
     * @param dailyAggregationDTO dailyAggregation record, holding the daily aggregation data retrieved from the api.
     * @return DailyAggregationData entity for the purpose of being used in saveDailyWeatherFromDTO method
     */
    public DailyAggregationData getDailyAggregationFromDTO(DailyTemperatureAggregationDTO dailyAggregationDTO) {
        DailyAggregationData dailyAggregationData = new DailyAggregationData();
        dailyAggregationData.setMorningTemperature(dailyAggregationDTO.morningTemperature());
        dailyAggregationData.setDayTemperature(dailyAggregationDTO.dayTemperature());
        dailyAggregationData.setEveningTemperature(dailyAggregationDTO.eveningTemperature());
        dailyAggregationData.setNightTemperature(dailyAggregationDTO.nightTemperature());
        dailyAggregationData.setMinimumDailyTemperature(dailyAggregationDTO.minimumDailyTemperature());
        dailyAggregationData.setMaximumDailyTemperature(dailyAggregationDTO.maximumDailyTemperature());

        dailyAggregationDataRepository.save(dailyAggregationData);
        return dailyAggregationData;
    }
    /**
     * Converts a dailyAggregationData entity to a DailyTemperatureAggregationDTO record.
     *
     * @param data DailyAggregationData entity, holding the daily aggregegation data retrieved from the database.
     * @return DailyTemperatureAggregationDTO for the purpose of displaying the weather data on the website.
     */
    public DailyTemperatureAggregationDTO converDailyAggregationToDTO(DailyAggregationData data){
        return new DailyTemperatureAggregationDTO(
                data.getMorningTemperature(),
                data.getDayTemperature(),
                data.getEveningTemperature(),
                data.getNightTemperature(),
                data.getMinimumDailyTemperature(),
                data.getMaximumDailyTemperature()
        );
    }
    /**
     * Converts a TemperatureAggregationDTO to a TemperatureAggregationData entity and saves it into the database.
     *
     * @param temperatureAggregationDTO temperatureAggregation record, holding the temperature aggregation data retrieved from the api.
     * @return TemperatureAggregationData entity for the purpose of being used in saveDailyWeatherFromDTO method
     */
    public TemperatureAggregationData getAggregationTemperatureFromDTO(TemperatureAggregationDTO temperatureAggregationDTO) {
        TemperatureAggregationData temperatureAggregationData = new TemperatureAggregationData();
        temperatureAggregationData.setMorningTemperature(temperatureAggregationDTO.morningTemperature());
        temperatureAggregationData.setDayTemperature(temperatureAggregationDTO.dayTemperature());
        temperatureAggregationData.setEveningTemperature(temperatureAggregationDTO.eveningTemperature());
        temperatureAggregationData.setNightTemperature(temperatureAggregationDTO.nightTemperature());

        temperatureAggregationDataRepository.save(temperatureAggregationData);
        return temperatureAggregationData;
    }
    /**
     * Converts a TemperatureAggregationData entity to a TemperatureAggregationDTO record.
     *
     * @param data TemperatureAggregationData entity, holding the temperature aggregegation data retrieved from the database.
     * @return TemperatureAggregationDTO for the purpose of displaying the weather data on the website.
     */
    public TemperatureAggregationDTO convertTempAggregationToDTO(TemperatureAggregationData data){
        return new TemperatureAggregationDTO(
                data.getMorningTemperature(),
                data.getDayTemperature(),
                data.getEveningTemperature(),
                data.getNightTemperature()
                );
    }
    /**
     * Converts a HourlyWeatherData entity to a HourlyWeatherDTO record.
     *
     * @param data HourlyWeatherData entity, holding the hourly weather data retrieved from the database.
     * @return HourlyWeatherDTO for the purpose of displaying the weather data on the website.
     */
    public HourlyWeatherDTO convertHourlyDataToDTO(HourlyWeatherData data){
        return new HourlyWeatherDTO(
          data.getTimestamp(),
          data.getTemperature(),
          data.getFeelsLikeTemperature(),
          data.getPressure(),
          data.getHumidity(),
          data.getDewPoint(),
          data.getUvi(),
          data.getClouds(),
          data.getVisibility(),
          data.getWindSpeed(),
          data.getWindGust(),
          data.getWindDirection(),
          data.getProbabilityOfPrecipitation(),
          data.getRain(), data.getSnow(),
                null
        );
    }
    /**
     * Converts a HourlyWeatherDTO to a HourlyWeatherData entity and saves it into the database.
     *
     * @param hourlyWeatherDTO hourlyWeather record, holding the hourly weather data retrieved from the api.
     * @param location name of the location corresponding to the hourly weather data.
     */
    public void saveHourlyWeatherFromDTO(HourlyWeatherDTO hourlyWeatherDTO, String location) {
        HourlyWeatherData hourlyWeatherData = new HourlyWeatherData();
        hourlyWeatherData.setTimestamp(hourlyWeatherDTO.timestamp());
        hourlyWeatherData.setTemperature(hourlyWeatherDTO.temperature());
        hourlyWeatherData.setFeelsLikeTemperature(hourlyWeatherDTO.feelsLikeTemperature());
        hourlyWeatherData.setPressure(hourlyWeatherDTO.pressure());
        hourlyWeatherData.setHumidity(hourlyWeatherDTO.humidity());
        hourlyWeatherData.setDewPoint(hourlyWeatherDTO.dewPoint());
        hourlyWeatherData.setWindSpeed(hourlyWeatherDTO.windSpeed());
        hourlyWeatherData.setWindGust(hourlyWeatherDTO.windGust());
        hourlyWeatherData.setWindDirection(hourlyWeatherDTO.windDirection());
        hourlyWeatherData.setClouds(hourlyWeatherDTO.clouds());
        hourlyWeatherData.setUvi(hourlyWeatherDTO.uvi());
        hourlyWeatherData.setProbabilityOfPrecipitation(hourlyWeatherDTO.probabilityOfPrecipitation());
        hourlyWeatherData.setRain(hourlyWeatherDTO.rain());
        hourlyWeatherData.setSnow(hourlyWeatherDTO.snow());
        hourlyWeatherData.setAdditionTime(Instant.now());
        hourlyWeatherData.setLocation(location);

        hourlyWeatherDataRepository.save(hourlyWeatherData);
    }

    public void saveCurrentWeatherFromDTO(CurrentWeatherDTO currentWeatherDTO, Location location) {
        CurrentWeatherData currentWeatherData = new CurrentWeatherData();
        currentWeatherData.setTimestamp(currentWeatherDTO.timestamp());
        currentWeatherData.setLocation(location);
        currentWeatherData.setSunrise(currentWeatherDTO.sunrise());
        currentWeatherData.setSunset(currentWeatherDTO.sunset());
        currentWeatherData.setTemperature(currentWeatherDTO.temperature());
        currentWeatherData.setFeelsLikeTemperature(currentWeatherDTO.feelsLikeTemperature());
        currentWeatherData.setPressure(currentWeatherDTO.pressure());
        currentWeatherData.setHumidity(currentWeatherDTO.humidity());
        currentWeatherData.setDewPoint(currentWeatherDTO.dewPoint());
        currentWeatherData.setClouds(currentWeatherDTO.clouds());
        currentWeatherData.setUvi(currentWeatherDTO.uvi());
        currentWeatherData.setVisibility(currentWeatherDTO.visibility());
        currentWeatherData.setRain(currentWeatherDTO.rain());
        currentWeatherData.setSnow(currentWeatherDTO.snow());
        currentWeatherData.setWindSpeed(currentWeatherDTO.windSpeed());
        currentWeatherData.setWindGust(currentWeatherDTO.windGust());
        currentWeatherData.setWindDirection(currentWeatherDTO.windDirection());

        // weatherDTO objects
        currentWeatherData.setMain(currentWeatherDTO.weather().title());
        currentWeatherData.setDescription(currentWeatherDTO.weather().description());
        currentWeatherData.setIcon(currentWeatherData.getIcon());

        currentWeatherData.setAdditionTime(Instant.now());

        currentWeatherDataRepository.save(currentWeatherData);
    }

    public CurrentWeatherDTO convertCurrentDataToDTO(CurrentWeatherData currentWeatherData) {
        WeatherDTO weatherDTO = new WeatherDTO(1,           // temp value of 1 for id
                currentWeatherData.getMain(),
                currentWeatherData.getDescription(),
                currentWeatherData.getIcon());

        return new CurrentWeatherDTO(
                currentWeatherData.getTimestamp(),
                currentWeatherData.getSunrise(),
                currentWeatherData.getSunset(),
                currentWeatherData.getTemperature(),
                currentWeatherData.getFeelsLikeTemperature(),
                currentWeatherData.getPressure(),
                currentWeatherData.getHumidity(),
                currentWeatherData.getDewPoint(),
                currentWeatherData.getClouds(),
                currentWeatherData.getUvi(),
                currentWeatherData.getVisibility(),
                currentWeatherData.getRain(),
                currentWeatherData.getSnow(),
                currentWeatherData.getWindSpeed(),
                currentWeatherData.getWindGust(),
                currentWeatherData.getWindDirection(),
                weatherDTO
        );
    }


}

