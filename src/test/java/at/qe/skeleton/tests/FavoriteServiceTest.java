package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.model.WeatherDataField;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.external.repositories.FavoriteRepository;
import at.qe.skeleton.internal.services.UserxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FavoriteServiceTest {
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private UserxService userxService;
    @Mock
    private LocationService locationService;
    @InjectMocks
    private FavoriteService favoriteService;
    private Userx testUser;
    private Favorite testFavorite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new Userx();
        testUser.setUsername("testUser");

        Location location = new Location();

        testFavorite = new Favorite();
        testFavorite.setId(1L);
        testFavorite.setUser(testUser);
        testFavorite.setLocation(location);
        testFavorite.setPriority(1);

    }

    @Test
    void testGetFavorites() {
        when(favoriteRepository.findByUser(testUser)).thenReturn(List.of(testFavorite));
        when(favoriteRepository.findByUserOrderByPriority(testUser)).thenReturn(List.of(testFavorite));

        List<Favorite> favorites = favoriteService.getSortedFavoritesList(testUser);

        assertEquals(1, favorites.size());
        assertEquals(testFavorite, favorites.get(0));
    }


    @Test
    void testGetFavoritesForUser() {
        when(userxService.getCurrentUser()).thenReturn(testUser);
        when(favoriteRepository.findByUser(testUser)).thenReturn(List.of(testFavorite));
        when(favoriteRepository.findByUserOrderByPriority(testUser)).thenReturn(List.of(testFavorite));
        List<Favorite> favorites = favoriteService.getSortedFavoritesForUser();

        assertEquals(1, favorites.size());
        assertEquals(testFavorite, favorites.get(0));
    }


    @Test
    void testDeleteFavorite() {
        favoriteService.deleteFavorite(testFavorite);

        verify(favoriteRepository, times(1)).delete(testFavorite);
    }

    @Test
    void testDeleteFavoriteById() {
        favoriteService.deleteFavoriteById(1L);

        verify(favoriteRepository, times(1)).deleteById(1L);
    }

    @Test
    void saveFavorite_LocationDoesNotExist_ThrowsEmptyLocationException() throws Exception {

        String locationName = "NonExistentLocation";
        when(locationService.retrieveLocation(locationName)).thenReturn(null);

        assertThrows(EmptyLocationException.class, () -> favoriteService.saveFavorite(locationName));
    }

    @Test
    void saveFavorite_CurrentUserIsNull_ThrowsEmptyLocationException() throws Exception {

        String locationName = "ExistingLocation";
        when(locationService.retrieveLocation(locationName)).thenReturn(new Location());
        when(userxService.getCurrentUser()).thenReturn(null);

        assertThrows(EmptyLocationException.class, () -> favoriteService.saveFavorite(locationName));
    }


    @Test
    void saveFavorite_SuccessfullySavesFavorite() throws Exception {

        String locationName = "ExistingLocation";
        Location location = new Location();
        when(locationService.retrieveLocation(locationName)).thenReturn(location);
        when(userxService.getCurrentUser()).thenReturn(new Userx());

        favoriteService.saveFavorite(locationName);


        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    public void testRetrieveSelectedFields() {

        List<WeatherDataField> resultField = new ArrayList<>();
        resultField.add(WeatherDataField.TEMP);
        resultField.add(WeatherDataField.FEELS_LIKE);
        resultField.add(WeatherDataField.DESCRIPTION);

        when(userxService.getSelectedWeatherFieldsForUser()).thenReturn(resultField);

        List<WeatherDataField> results =  favoriteService.retrieveSelectedFields();

        assertEquals(results.get(0), resultField.get(0));
        assertEquals(results.get(1), resultField.get(1));
        assertEquals(results.get(2), resultField.get(2));

        favoriteService.addSelectedFields(List.of(WeatherDataField.PRESSURE));

        //assertEquals(results);
    }

    @Test
    public void testAddSelectedFields() {
        // Mock the behavior of userxService.getCurrentUser()
        Userx mockUser = new Userx();
        when(userxService.getCurrentUser()).thenReturn(mockUser);

        List<WeatherDataField> resultField = new ArrayList<>();
        resultField.add(WeatherDataField.TEMP);
        resultField.add(WeatherDataField.FEELS_LIKE);
        resultField.add(WeatherDataField.DESCRIPTION);
        resultField.add(WeatherDataField.PRESSURE);

        when(userxService.getSelectedWeatherFieldsForUser()).thenReturn(resultField);

        favoriteService.addSelectedFields(List.of(WeatherDataField.PRESSURE));

        List<WeatherDataField> result = favoriteService.retrieveSelectedFields();

        assertEquals(result, resultField);
    }

    @Test
    public void testDeleteSelectedFields() {
        // Mock the behavior of userxService.getCurrentUser()
        Userx mockUser = new Userx();
        mockUser.setSelectedFields(List.of(WeatherDataField.TEMP,
                WeatherDataField.FEELS_LIKE,
                WeatherDataField.DESCRIPTION));
        when(userxService.getCurrentUser()).thenReturn(mockUser);

        List<WeatherDataField> initialSelectedFields = new ArrayList<>();
        initialSelectedFields.add(WeatherDataField.TEMP);
        initialSelectedFields.add(WeatherDataField.FEELS_LIKE);
        initialSelectedFields.add(WeatherDataField.DESCRIPTION);
        initialSelectedFields.add(WeatherDataField.PRESSURE);

        when(userxService.getSelectedWeatherFieldsForUser()).thenReturn(mockUser.getSelectedFields());


        favoriteService.deleteSelectedFields(List.of(WeatherDataField.PRESSURE));

        verify(userxService, times(1))
                .deleteSelectedWeatherFieldsForUser(List.of(WeatherDataField.PRESSURE));

        List<WeatherDataField> updatedSelectedFields = favoriteService.retrieveSelectedFields();
        List<WeatherDataField> expectedUpdatedFields = Arrays.asList(
                WeatherDataField.TEMP,
                WeatherDataField.FEELS_LIKE,
                WeatherDataField.DESCRIPTION
        );

        List<WeatherDataField> result = mockUser.getSelectedFields();

        assertEquals(result, expectedUpdatedFields);
    }


    @Test
    public void testSetDefaultSelectedFields() {
        Userx mockUser = new Userx();
        when(userxService.getCurrentUser()).thenReturn(mockUser);

        doAnswer(invocation -> {
            List<WeatherDataField> argument = invocation.getArgument(0);
            mockUser.setSelectedFields(argument);
            return null;
        }).when(userxService).addSelectedWeatherFieldsForUser(anyList());

        favoriteService.setDefaultSelectedFields();

        assertEquals(List.of(
                WeatherDataField.TEMP,
                WeatherDataField.FEELS_LIKE,
                WeatherDataField.DESCRIPTION), mockUser.getSelectedFields());
    }

}
