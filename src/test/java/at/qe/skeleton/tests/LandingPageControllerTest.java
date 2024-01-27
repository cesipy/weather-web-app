package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.controllers.LandingPageController;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.external.controllers.MessageService;
import at.qe.skeleton.internal.services.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LandingPageControllerTest {

    @InjectMocks
    private LandingPageController landingPageController;

    @Mock
    private AuditLogService.WeatherService weatherService;

    @Mock
    private LocationService locationService;

    @Mock
    private MessageService messageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void landingPageWeatherIsFetchedForDefaultFavorites() throws ApiQueryException {
        Favorite favorite = new Favorite();
        Location mockLocation = new Location();
        mockLocation.setId(1L);
        favorite.setLocation(mockLocation);
        CurrentWeatherData currentWeatherData = new CurrentWeatherData();
        when(weatherService.fetchCurrentWeather(favorite.getLocation())).thenReturn(currentWeatherData);

        landingPageController.init();

        List<CurrentWeatherData> landingPageWeather = landingPageController.getLandingPageWeather();
        assertEquals(3, landingPageWeather.size());
        //assertEquals(currentWeatherData, landingPageWeather.get(0));
    }


    @Test
    public void defaultFavoritesAreConstructed() throws EmptyLocationException, ApiQueryException {
        landingPageController.init();

        verify(locationService, times(1)).retrieveLocation("Innsbruck");
        verify(locationService, times(1)).retrieveLocation("Vienna");
        verify(locationService, times(1)).retrieveLocation("Salzburg");
    }

    @Test
    public void errorIsLoggedWhenDefaultFavoritesConstructionFails() throws EmptyLocationException, ApiQueryException {
        when(locationService.retrieveLocation(anyString())).thenThrow(EmptyLocationException.class);

        landingPageController.init();

        verify(messageService, times(1)).showWarnMessage("An error occurred while default locations were processed.");
    }
}