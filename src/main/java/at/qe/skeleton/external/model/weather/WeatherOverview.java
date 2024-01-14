package at.qe.skeleton.external.model.weather;

import at.qe.skeleton.external.model.WeatherDataField;

public class WeatherOverview {
    private WeatherDataField weatherDataField;
    private String value;

    public WeatherDataField getWeatherDataField() {
        return weatherDataField;
    }

    public void setWeatherDataField(WeatherDataField weatherDataField) {
        this.weatherDataField = weatherDataField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
