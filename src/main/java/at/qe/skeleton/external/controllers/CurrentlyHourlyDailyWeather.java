package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;

import java.util.List;

public class CurrentlyHourlyDailyWeather {
    private List<HourlyWeatherDTO> hourlyWeatherList;
    private List<DailyWeatherDTO> dailyWeatherList;

    public CurrentlyHourlyDailyWeather(List<HourlyWeatherDTO> hourlyWeatherList, List<DailyWeatherDTO> dailyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
        this.dailyWeatherList = dailyWeatherList;
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
