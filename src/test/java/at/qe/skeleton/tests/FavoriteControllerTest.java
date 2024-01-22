package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.controllers.FavoriteController;
import at.qe.skeleton.external.controllers.MessageService;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.repositories.FavoriteRepository;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.LocationAlreadyInFavoritesException;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.UserxService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FavoriteControllerTest {
    @Mock
    private FavoriteService favoriteService;
    @InjectMocks
    private FavoriteController favoriteController;
    @Mock
    private UserxService userxService;
    @Mock
    private MessageService messageService;

    @Mock
    private FavoriteRepository favoriteRepository;
    private Location mockLocation1;
    private Location mockLocation2;
    private Userx mockUser;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(favoriteController, "locationName", "TestLocation");

        mockLocation1 = new Location(1L, "Innsbruck", 47.0,
                13.0, "AT", "Austria", "1111");
        mockLocation2 = new Location(2L, "Vienna", 40.0,
                13.0, "AT", "Austria", "2222");

        // do nothing, because I did not manage to test FacesMessages separately
        doNothing().when(messageService).showWarnMessage(anyString());
        doNothing().when(messageService).showInfoMessage(anyString());
        mockUser = new Userx();
    }


    @Test
    void testSaveFavorite_SuccessfulSave() throws EmptyLocationException, LocationAlreadyInFavoritesException, ApiQueryException {
        when(favoriteService.isLocationAlreadyFavorite("TestLocation")).thenReturn(false);

        favoriteController.saveFavorite();

        verify(favoriteService, times(1)).saveFavorite(eq("TestLocation"));

        assert(favoriteController.getLocationName().isEmpty());
    }


    @Test
    public void testRetrieveFavorites() {

        Favorite favorite1 = new Favorite();
        favorite1.setLocation(mockLocation1);
        favorite1.setPriority(1);

        Favorite favorite2 = new Favorite();
        favorite2.setPriority(2);
        favorite2.setLocation(mockLocation2);

        when(favoriteService.getSortedFavoritesForUser())
                .thenReturn(List.of(favorite1, favorite2));

        when(userxService.getCurrentUser()).thenReturn(mockUser);

        favoriteController.retrieveFavorites();

        verify(favoriteService).getSortedFavoritesForUser();

        List<Location> locations = favoriteController.getLocations();
        assertEquals(favoriteController.getFavorites().size(), 2);
        assertEquals(List.of(mockLocation1, mockLocation2), locations);
    }


    @Test
    void testDeleteFavorite_SuccessfulDelete() {

        Favorite favorite1 = new Favorite();
        favorite1.setLocation(mockLocation1);
        favorite1.setPriority(1);

        favoriteController.deleteFavorite(favorite1);

        List<Location> locations = favoriteController.getLocations();
        assertEquals(0, locations.size());

    }

    @Test
    public void testAutocomplete() throws EmptyLocationException {
        String query = "Innsbruck";
        favoriteController.setLocationName(query);

        when(favoriteService.autocomplete(query))
                .thenReturn(List.of(mockLocation1));
        List<Location> locations =  favoriteController.autocomplete(query);

        assertEquals(query, locations.get(0).getName());

    }


    @Test
    public void testAutocompleteEmpty() throws EmptyLocationException {
        String query = "  ";

        when(favoriteService.autocomplete(query))
                .thenThrow(EmptyLocationException.class);

        List<Location> locations = favoriteController.autocomplete(query);

        assertEquals(Collections.emptyList(), locations);
    }

    @Test
    void testMoveFavoriteUp() {
        Favorite favorite = new Favorite();  // Create a sample Favorite object

        favoriteController.moveFavoriteUp(favorite);

        // verify that the moveFavoriteUpOrDown method is called with the expected parameters
        // test for moveFavorite is already tested
        verify(favoriteService).moveFavoriteUpOrDown(favorite, true);
    }

    @Test
    void testMoveFavoriteDown() {
        Favorite favorite = new Favorite();  // Create a sample Favorite object

        favoriteController.moveFavoriteDown(favorite);


        // verify that the moveFavoriteUpOrDown method is called with the expected parameters
        // test for moveFavorite is already tested
        verify(favoriteService).moveFavoriteUpOrDown(favorite, false);
    }

    @Test
    void testEmptyLocationExceptionWithoutMessage() {
        EmptyLocationException exception = new EmptyLocationException();

        assertNull(exception.getMessage());
    }

    @Test
    void testSaveFavorite_LocationAlreadyInFavorites() throws EmptyLocationException, ApiQueryException {
        when(favoriteService.isLocationAlreadyFavorite("TestLocation")).thenReturn(true);

        favoriteController.saveFavorite();

        verify(messageService).showWarnMessage("Location already in favorites: TestLocation");
    }


    @Test
    void testSaveFavorite_ApiQueryException() throws EmptyLocationException, ApiQueryException {
        when(favoriteService.isLocationAlreadyFavorite("TestLocation")).thenReturn(false);
        doThrow(new ApiQueryException("Test API query exception")).when(favoriteService).saveFavorite("TestLocation");

        favoriteController.saveFavorite();

        verify(messageService).showWarnMessage("Error occurred while fetching weather data");
    }

    @Test
    void testSaveFavorite_GeneralException() throws EmptyLocationException, ApiQueryException {
        when(favoriteService.isLocationAlreadyFavorite("TestLocation")).thenReturn(false);
        doThrow(new RuntimeException("Test general exception")).when(favoriteService).saveFavorite("TestLocation");

        favoriteController.saveFavorite();

        verify(messageService).showWarnMessage("An error occurred!");
    }

}
