package at.qe.skeleton.external.domain;

import at.qe.skeleton.external.domain.DailyAggregationData;
import at.qe.skeleton.external.domain.TemperatureAggregationData;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "weather_data")
public class DailyWeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weather_id;

    private Instant timestamp;
    private Instant sunrise;
    private Instant sunset;
    private Instant moonrise;
    private Instant moonset;
    private double moonPhase;
    private String summary;
    @OneToOne
    @JoinColumn(name = "daily_aggregation_id")
    private DailyAggregationData dailyTemperatureAggregation;
    @OneToOne
    @JoinColumn(name = "temp_aggregation_id")
    private TemperatureAggregationData feelsLikeTemperatureAggregation;
    private int pressure;
    private int humidity;
    private double dewPoint;
    private double windSpeed;
    private double windGust;
    private double windDirection;
    private int clouds;
    private int uvi;
    private int probabilityOfPrecipitation;
    private Double rain;
    private Double snow;
    private Instant additionTime;
    private String location;


    public DailyWeatherData() {
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

    public Long getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(Long weather_id) {
        this.weather_id = weather_id;
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

    public Instant getSunset() {
        return sunset;
    }

    public void setSunset(Instant sunset) {
        this.sunset = sunset;
    }

    public Instant getMoonrise() {
        return moonrise;
    }

    public void setMoonrise(Instant moonrise) {
        this.moonrise = moonrise;
    }

    public Instant getMoonset() {
        return moonset;
    }

    public void setMoonset(Instant moonset) {
        this.moonset = moonset;
    }

    public double getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(double moonPhase) {
        this.moonPhase = moonPhase;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public DailyAggregationData getDailyTemperatureAggregation() {
        return dailyTemperatureAggregation;
    }

    public void setDailyTemperatureAggregation(DailyAggregationData dailyTemperatureAggregation) {
        this.dailyTemperatureAggregation = dailyTemperatureAggregation;
    }

    public TemperatureAggregationData getFeelsLikeTemperatureAggregation() {
        return feelsLikeTemperatureAggregation;
    }

    public void setFeelsLikeTemperatureAggregation(TemperatureAggregationData feelsLikeTemperatureAggregation) {
        this.feelsLikeTemperatureAggregation = feelsLikeTemperatureAggregation;
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

    public Double getSnow() {
        return snow;
    }

    public void setSnow(Double snow) {
        this.snow = snow;
    }
}

