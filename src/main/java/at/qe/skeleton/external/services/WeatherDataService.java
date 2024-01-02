package at.qe.skeleton.external.services;

import at.qe.skeleton.external.domain.DailyAggregationData;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.TemperatureAggregationData;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyTemperatureAggregationDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.TemperatureAggregationDTO;
import at.qe.skeleton.internal.repositories.DailyAggregationDataRepository;
import at.qe.skeleton.internal.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.internal.repositories.TemperatureAggregationDataRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.time.Instant;
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

    public void saveDailyWeatherFromDTO(DailyWeatherDTO dailyWeatherDTO, String location) {
        DailyWeatherData dailyWeatherData = new DailyWeatherData();
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

    public DailyWeatherDTO convertDailyDataToDTO(DailyWeatherData data){
        return new DailyWeatherDTO(
                data.getTimestamp(),
                data.getSunrise(),
                data.getSunset(),
                data.getMoonrise(),
                data.getMoonset(),
                data.getMoonPhase(),
                data.getSummary(),
                converDailyAggregationToDTO(data.getDailyTemperatureAggregation()), // assuming you have a similar method for this conversion
                convertTempAggregationToDTO(data.getFeelsLikeTemperatureAggregation()), // assuming you have a similar method for this conversion
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
    public TemperatureAggregationData getAggregationTemperatureFromDTO(TemperatureAggregationDTO temperatureAggregationDTO) {
        TemperatureAggregationData temperatureAggregationData = new TemperatureAggregationData();
        temperatureAggregationData.setMorningTemperature(temperatureAggregationDTO.morningTemperature());
        temperatureAggregationData.setDayTemperature(temperatureAggregationDTO.dayTemperature());
        temperatureAggregationData.setEveningTemperature(temperatureAggregationDTO.eveningTemperature());
        temperatureAggregationData.setNightTemperature(temperatureAggregationDTO.nightTemperature());

        temperatureAggregationDataRepository.save(temperatureAggregationData);
        return temperatureAggregationData;
    }
    public TemperatureAggregationDTO convertTempAggregationToDTO(TemperatureAggregationData data){
        return new TemperatureAggregationDTO(
                data.getMorningTemperature(),
                data.getDayTemperature(),
                data.getEveningTemperature(),
                data.getNightTemperature()
                );
    }
}
