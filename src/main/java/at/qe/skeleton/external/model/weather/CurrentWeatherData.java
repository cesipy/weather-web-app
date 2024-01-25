package at.qe.skeleton.external.model.weather;

import at.qe.skeleton.external.model.location.Location;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * This class represents the current weather data for a specific location. It includes various
 * weather-related attributes such as temperature, humidity, wind speed, and more. The class also provides methods
 * for formatting sunrise and sunset times.
 */
@Entity
public class CurrentWeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private Instant timestamp;
    private Instant sunrise;
    private Instant sunset;
    private double temperature;
    private double feelsLikeTemperature;
    private int pressure;
    private int humidity;
    private double dewPoint;
    private int clouds;
    private int uvi;
    private int visibility;
    private Double rain;
    private Double snow;
    private double windSpeed;
    private double windGust;
    private double windDirection;
    private String main;
    private String description;
    private String icon;
    private Instant additionTime;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Instant getAdditionTime() {
        return additionTime;
    }

    public void setAdditionTime(Instant additionTime) {
        this.additionTime = additionTime;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Instant getSunrise() {
        return sunrise;
    }

    public void setSunrise(Instant sunrise) {
        this.sunrise = sunrise;
    }

    /**
     * Gets the formatted sunrise time in "HH'h':mm'm'" format.
     *
     * @return The formatted sunrise time.
     */
    public String getSunriseFormatted() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH'h':mm'm'");
        return dateTimeFormatter.format(sunrise.atZone(ZoneId.of("CET")));
    }

    public Instant getSunset() {
        return sunset;
    }

    public void setSunset(Instant sunset) {
        this.sunset = sunset;
    }

    /**
     * Gets the formatted sunset time in "HH'h':mm'm'" format.
     *
     * @return The formatted sunset time.
     */
    public String getSunsetFormatted() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH'h':mm'm'");
        return dateTimeFormatter.format(sunset.atZone(ZoneId.of("CET")));
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

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getUvi() {
        return uvi;
    }

    public void setUvi(int uvi) {
        this.uvi = uvi;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public Double getSnow() {
        return snow;
    }

    public void setSnow(Double snow) {
        this.snow = snow;
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

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "CurrentWeatherData{" +
                "id=" + id +
                ", location_id=" + location.getId() +
                ", timestamp=" + timestamp +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                ", temperature=" + temperature +
                ", feelsLikeTemperature=" + feelsLikeTemperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", dewPoint=" + dewPoint +
                ", clouds=" + clouds +
                ", uvi=" + uvi +
                ", visibility=" + visibility +
                ", rain=" + rain +
                ", snow=" + snow +
                ", windSpeed=" + windSpeed +
                ", windGust=" + windGust +
                ", windDirection=" + windDirection +
                ", title='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", additionTime=" + additionTime +
                '}';
    }
}
