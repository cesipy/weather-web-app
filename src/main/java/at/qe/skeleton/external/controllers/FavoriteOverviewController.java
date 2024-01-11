package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.shared.WeatherDTO;
import at.qe.skeleton.external.model.weather.WeatherOverview;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.model.Favorite;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("view")
public class FavoriteOverviewController {
    private static Logger LOGGER = LoggerFactory.getLogger(FavoriteOverviewController.class);
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    WeatherApiRequestService weatherApiRequestService;
    List<Favorite> favorites;
    List<CurrentAndForecastAnswerDTO> weatherList;
    List<WeatherOverview> weatherOverviewList;

    @PostConstruct
    public void init() {
        favorites = new ArrayList<>();
        weatherList = new ArrayList<>();
        weatherOverviewList = new ArrayList<>();

        // needed to refresh favorites
        retrieveFavorites();
    }

    public void retrieveFavorites() {
        // clear the existing list before retrieving new favorites
        weatherOverviewList.clear();

        favorites = favoriteService.getSortedFavoritesForUser();

        if (favorites.isEmpty()) {
            LOGGER.info("favorites in overview are empty");
        }

        for (Favorite favorite : favorites) {
            Location location = favorite.getLocation();

            CurrentAndForecastAnswerDTO weather = weatherApiRequestService
                    .retrieveCurrentAndForecastWeather(location.getLatitude(), location.getLongitude());
            weatherList.add(weather);

            double temperature = weather.currentWeather().temperature();
            double feelsLikeTemperature = weather.currentWeather().feelsLikeTemperature();
            WeatherDTO weatherDTO = weather.currentWeather().weather();

            WeatherOverview weatherOverview = new WeatherOverview(temperature,
                    feelsLikeTemperature, weatherDTO, location);

            weatherOverviewList.add(weatherOverview);
             LOGGER.info("current weather for location: " + location + ": " + weather.currentWeather());
            }

    }


    public List<WeatherOverview> getWeatherOverviewList() {
        return weatherOverviewList;
    }

    public void setWeatherOverviewList(List<WeatherOverview> weatherOverviewList) {
        this.weatherOverviewList = weatherOverviewList;
    }
}
