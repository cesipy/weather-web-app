package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.controllers.FavoriteController;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.LocationAlreadyInFavoritesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

public class FavoriteControllerTest {
    @Mock
    private FavoriteService favoriteService;

    @InjectMocks
    private FavoriteController favoriteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(favoriteController, "locationName", "TestLocation");
    }



    @Test
    void testSaveFavorite_SuccessfulSave() throws EmptyLocationException, LocationAlreadyInFavoritesException {
        when(favoriteService.isLocationAlreadyFavorite("TestLocation")).thenReturn(false);

        favoriteController.saveFavorite();

        // Assert that saveFavorite was called on the service
        verify(favoriteService, times(1)).saveFavorite(eq("TestLocation"));

        // Assert that locationName is cleared
        assert(favoriteController.getLocationName().isEmpty());
    }

}
