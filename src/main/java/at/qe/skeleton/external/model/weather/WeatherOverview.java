package at.qe.skeleton.external.model.weather;


import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.shared.WeatherDTO;

public class WeatherOverview {
    private double temperature;
    private double feelsLikeTemperature;
    private WeatherDTO weather;
    private Location location;
    private String weatherTitleAndDescription;

    public WeatherOverview(double temperature, double feelsLikeTemperature, WeatherDTO weather, Location location) {
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.weather = weather;
        this.location = location;
        this.weatherTitleAndDescription = weather.title() + ", " + weather.description();
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

    public WeatherDTO getWeather() {
        return weather;
    }

    public void setWeather(WeatherDTO weather) {
        this.weather = weather;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getWeatherTitleAndDescription() {
        return weatherTitleAndDescription;
    }

    public void setWeatherTitleAndDescription(String weatherTitleAndDescription) {
        this.weatherTitleAndDescription = weatherTitleAndDescription;
    }
}
