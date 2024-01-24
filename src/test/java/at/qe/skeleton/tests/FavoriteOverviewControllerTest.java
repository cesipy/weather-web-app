package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.FavoriteOverviewController;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.model.WeatherDataField;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.external.services.*;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.UserxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FavoriteOverviewControllerTest {
    @InjectMocks
    private FavoriteOverviewController favoriteOverviewController;
    @Mock
    private FavoriteService favoriteService;
    @Mock
    private WeatherApiRequestService weatherApiRequestService;
    @Mock
    private WeatherDataService weatherDataService;
    @Mock
    private CurrentWeatherDataRepository currentWeatherDataRepository;
    @Mock
    private WeatherService weatherService;
    @Mock
    private LocationService locationService;
    @Mock
    private UserxService userxService;

    private Location mockLocation1;
    private Location mockLocation2;
    private Userx mockUser;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockLocation1 = new Location(1L, "Innsbruck", 47.0, 13.0, "AT", "Austria", "1111");
        mockLocation2 = new Location(2L, "Vienna", 40.0, 13.0, "AT", "Austria", "2222");

        mockUser = new Userx();

        favoriteOverviewController.setFavorites(new ArrayList<>());
        favoriteOverviewController.setCurrentWeatherDataList(new ArrayList<>());
        favoriteOverviewController.setSelectedFieldList(new ArrayList<>());
    }

    @Test
    public void testRetrieveFavorites() throws ApiQueryException {
        Favorite favorite1 = new Favorite();
        favorite1.setLocation(mockLocation1);
        favorite1.setPriority(1);
        favorite1.setId(1L);

        Favorite favorite2 = new Favorite();
        favorite2.setLocation(mockLocation2);
        favorite2.setPriority(1);
        favorite2.setId(1L);

        List<WeatherDataField> selectedFields = List.of(WeatherDataField.TEMP,
                WeatherDataField.FEELS_LIKE,
                WeatherDataField.DESCRIPTION);

        when(favoriteService.getSortedFavoritesForUser())
                .thenReturn(List.of(favorite1, favorite2));

        when(favoriteService.retrieveSelectedFields())
                .thenReturn(selectedFields);

        CurrentWeatherData mockWeatherData = new CurrentWeatherData();
        mockWeatherData.setFeelsLikeTemperature(10);
        mockWeatherData.setLocation(mockLocation1);

        when(weatherService.fetchCurrentWeather(any()))
                .thenReturn(mockWeatherData);

        favoriteOverviewController.retrieveFavorites();

        assertEquals(2, favoriteOverviewController.getCurrentWeatherDataList().size());
        assertEquals(mockLocation1, favoriteOverviewController.getCurrentWeatherDataList().get(0).getLocation());
        assertEquals(mockLocation2, favoriteOverviewController.getFavorites().get(1).getLocation());
        assertEquals(selectedFields, favoriteOverviewController.getSelectedFieldList());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16})
    void testOnToggle_Visual(int columnIndex) {
        ToggleEvent toggleEvent = mock(ToggleEvent.class);
        Visibility visibility = Visibility.VISIBLE;

        when(toggleEvent.getVisibility()).thenReturn(visibility);
        when(toggleEvent.getData()).thenReturn(columnIndex);

        assertEquals(0, favoriteOverviewController.getSelectedFieldList().size());

        favoriteOverviewController.onToggle(toggleEvent);

        WeatherDataField expectedField = getExpectedFieldForColumnIndex(columnIndex);
        verify(favoriteService, times(1)).addSelectedFields(List.of(expectedField));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16})
    void testOnToggle_Hide(int columnIndex) {
        List<WeatherDataField> selectedFields = new ArrayList<>(Arrays.asList(WeatherDataField.values()));

        favoriteOverviewController.setSelectedFieldList(selectedFields);
        ToggleEvent toggleEvent = mock(ToggleEvent.class);
        Visibility visibility = Visibility.HIDDEN;

        when(toggleEvent.getVisibility()).thenReturn(visibility);
        when(toggleEvent.getData()).thenReturn(columnIndex);

        assertEquals(16, favoriteOverviewController.getSelectedFieldList().size());

        favoriteOverviewController.onToggle(toggleEvent);

        WeatherDataField expectedField = getExpectedFieldForColumnIndex(columnIndex);
        verify(favoriteService, times(1)).deleteSelectedFields(List.of(expectedField));
    }

    private WeatherDataField getExpectedFieldForColumnIndex(int column) {
        return switch (column) {
            case 1 -> WeatherDataField.SUNRISE;
            case 2 -> WeatherDataField.SUNSET;
            case 3 -> WeatherDataField.TEMP;
            case 4 -> WeatherDataField.FEELS_LIKE;
            case 5 -> WeatherDataField.PRESSURE;
            case 6 -> WeatherDataField.HUMIDITY;
            case 7 -> WeatherDataField.DEW_POINT;
            case 8 -> WeatherDataField.CLOUDS;
            case 9 -> WeatherDataField.UVI;
            case 10 -> WeatherDataField.VISIBILITY;
            case 11 -> WeatherDataField.WIND_SPEED;
            case 12 -> WeatherDataField.WIND_DIRECTION;
            case 13 -> WeatherDataField.RAIN;
            case 14 -> WeatherDataField.SNOW;
            case 15 -> WeatherDataField.ICON;
            case 16 -> WeatherDataField.DESCRIPTION;
            default -> null;
        };
    }

    @Test
    void testIsInList_FieldPresent() {

        WeatherDataField fieldToFind = WeatherDataField.TEMP;
        List<WeatherDataField> selectedFieldList = List.of(fieldToFind, WeatherDataField.TEMP, WeatherDataField.HUMIDITY);
        favoriteOverviewController.setSelectedFieldList(selectedFieldList);

        boolean result = favoriteOverviewController.isInList(fieldToFind.name());

        assertTrue(result);
    }

    @Test
    void testIsInList_FieldNotPresent() {
        WeatherDataField fieldToFind = WeatherDataField.SUNRISE;
        List<WeatherDataField> selectedFieldList = List.of(WeatherDataField.TEMP, WeatherDataField.HUMIDITY);
        favoriteOverviewController.setSelectedFieldList(selectedFieldList);

        boolean result = favoriteOverviewController.isInList(fieldToFind.name());

        assertFalse(result);
    }

    @Test
    void testIsInList_EmptyList() {

        WeatherDataField fieldToFind = WeatherDataField.SUNRISE;
        List<WeatherDataField> selectedFieldList = new ArrayList<>();
        favoriteOverviewController.setSelectedFieldList(selectedFieldList);

        boolean result = favoriteOverviewController.isInList(fieldToFind.name());

        assertFalse(result);
    }


}
