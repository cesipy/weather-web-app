package at.qe.skeleton.external.controllers;

import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.services.WeatherService;
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
public class LandingPageController {
    private final static Logger logger = LoggerFactory.getLogger(LandingPageController.class);

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private MessageService messageService;

    private List<Favorite> defaultFavorites;
    private List<CurrentWeatherData> landingPageWeather;

    @PostConstruct
    public void init() {
        defaultFavorites = new ArrayList<>();
        landingPageWeather = new ArrayList<>();
        constructDefaultFavorites();
        getWeatherForLandingPage();

    }


    private void getWeatherForLandingPage() {
        try {

            for (Favorite favorite : defaultFavorites) {
                CurrentWeatherData currentWeatherData = weatherService.fetchCurrentWeather(favorite.getLocation());
                landingPageWeather.add(currentWeatherData);
            }
        }
        catch (ApiQueryException e) {
            messageService.showWarnMessage("An error occurred.");
        }
    }

    private void constructDefaultFavorites() {
        try {
            Favorite innsbruckFavorite = new Favorite();
            Favorite viennaFavorite = new Favorite();
            Favorite salzburgFavorite = new Favorite();

            Location innsbruckLocation = locationService.retrieveLocation("Innsbruck");
            Location viennaLocation = locationService.retrieveLocation("Vienna");
            Location salzburgLocation = locationService.retrieveLocation("Salzburg");
            innsbruckFavorite.setLocation(innsbruckLocation);
            viennaFavorite.setLocation(viennaLocation);
            salzburgFavorite.setLocation(salzburgLocation);

            defaultFavorites.add(innsbruckFavorite);
            defaultFavorites.add(viennaFavorite);
            defaultFavorites.add(salzburgFavorite);
        }
        catch (Exception e) {
            logger.error("An error occurred while default locations were processed.");
        }
    }

    public List<CurrentWeatherData> getLandingPageWeather() {
        return landingPageWeather;
    }

}
