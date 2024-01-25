package at.qe.skeleton.external.domain;

import jakarta.persistence.*;
import java.time.Instant;
/**
 * Represents the hourly weather data.
 * <p>
 *     This class is used to store and retrieve the hourly weather data
 *     in and from the database.
 * </p>
 */
@Entity
@Table(name = "hourly_weather_data")
public class HourlyWeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hourlyweatherId;
    private Instant timestamp;
    private double temperature;
    private double feelsLikeTemperature;
    private int pressure;
    private double humidity;
    private double dewPoint;
    private int uvi;
    private int clouds;
    private int visibility;
    private double windSpeed;
    private double windGust;
    private double windDirection;
    private int probabilityOfPrecipitation;
    private Double rain;
    private Double snow;
    private Instant additionTime;
    private String location;
    public HourlyWeatherData() {
    }

    public HourlyWeatherData(Instant timestamp, double temperature, double feelsLikeTemperature, int pressure, double humidity, double dewPoint, int uvi, int clouds, int visibility, double windSpeed, double windGust, double windDirection, int probabilityOfPrecipitation, Double rain, Double snow, Instant additionTime, String location) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dewPoint = dewPoint;
        this.uvi = uvi;
        this.clouds = clouds;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windGust = windGust;
        this.windDirection = windDirection;
        this.probabilityOfPrecipitation = probabilityOfPrecipitation;
        this.rain = rain;
        this.snow = snow;
        this.additionTime = additionTime;
        this.location = location;
    }

    public Long getHourlyweatherId() {
        return hourlyweatherId;
    }

    public void setHourlyweatherId(Long hourlyweatherId) {
        this.hourlyweatherId = hourlyweatherId;
    }

    public Instant getAdditionTime() {
        return additionTime;
    }

    public void setAdditionTime(Instant additionTime) {
        this.additionTime = additionTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getSnow() {
        return snow;
    }

    public void setSnow(Double snow) {
        this.snow = snow;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(double feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public int getUvi() {
        return uvi;
    }

    public void setUvi(int uvi) {
        this.uvi = uvi;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindGust() {
        return windGust;
    }

    public void setWindGust(double windGust) {
        this.windGust = windGust;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public int getProbabilityOfPrecipitation() {
        return probabilityOfPrecipitation;
    }

    public void setProbabilityOfPrecipitation(int probabilityOfPrecipitation) {
        this.probabilityOfPrecipitation = probabilityOfPrecipitation;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }
}
