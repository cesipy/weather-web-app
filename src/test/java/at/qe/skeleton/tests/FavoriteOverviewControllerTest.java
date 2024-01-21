package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.FavoriteOverviewController;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteOverviewControllerTest {

    private MockMvc mvc;
    @InjectMocks
    private FavoriteOverviewController favoriteOverviewController;

    @Mock
    private FavoriteService favoriteService;

    @Mock
    private WeatherApiRequestService weatherApiRequestService;

    @Mock
    private CurrentWeatherDataRepository currentWeatherDataRepository;

    @Mock
    private WeatherDataService weatherDataService;

    @BeforeEach
    public void setUp() {
        favoriteOverviewController.init();
    }

    @AfterEach
    public void tearDown() {
        favoriteOverviewController.init();
    }

    @Test
    public void testRetrieveFavorites() {
        Location mockLocation1 = new Location();
        mockLocation1.setId(1L);
        mockLocation1.setLatitude(0.1);
        mockLocation1.setLongitude(2.0);
        mockLocation1.setName("Innsbruck");

        Favorite mockFavorite1 = new Favorite();
        mockFavorite1.setLocation(mockLocation1);

        CurrentAndForecastAnswerDTO currentWeather1 = new CurrentAndForecastAnswerDTO(0, 0, null, 0,
                null, null, null, null, null);

        CurrentWeatherData currentWeatherData1 = new CurrentWeatherData();
        currentWeatherData1.setLocation(mockLocation1);
        currentWeatherData1.setAdditionTime(Instant.now());

        List<Favorite> mockFavorites = new ArrayList<>();
        when(favoriteService.getSortedFavoritesForUser()).thenReturn(mockFavorites);

        doReturn(List.of(currentWeatherData1))
                .when(currentWeatherDataRepository)
                .findByLocationOrderByAdditionTimeDesc(mockLocation1);

        favoriteOverviewController.retrieveFavorites();

        List<Favorite> favorites = favoriteOverviewController.getFavorites();
        List<CurrentWeatherData> currentWeatherDatas = favoriteOverviewController.getCurrentWeatherDataList();

        assertEquals(favorites.size(), 0,
                "favorite list is empty when there are no favorites");
        assertEquals(currentWeatherDatas.size(),0,
                "weather data list is empty when there are no favorites");

        mockFavorites.add(mockFavorite1);

        favorites = favoriteOverviewController.getFavorites();
        currentWeatherDatas = favoriteOverviewController.getCurrentWeatherDataList();

        favoriteOverviewController.retrieveFavorites();

        assertEquals(1, favorites.size());
        assertEquals(1, currentWeatherDatas.size());
    }

}
