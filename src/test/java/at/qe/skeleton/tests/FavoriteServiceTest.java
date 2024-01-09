package at.qe.skeleton.tests;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.LocationAlreadyInFavoritesException;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.FavoriteRepository;
import at.qe.skeleton.internal.services.UserxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

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
        favoriteService.init();
    }

    @Test
    void testGetFavorites() {
        when(favoriteRepository.findByUser(testUser)).thenReturn(List.of(testFavorite));

        List<Favorite> favorites = favoriteService.getFavorites(testUser);

        assertEquals(1, favorites.size());
        assertEquals(testFavorite, favorites.get(0));
    }

    @Test
    void testUpdateFavoritePriority() {
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(testFavorite));

        favoriteService.updateFavoritePriority(1L, 5);

        assertEquals(5, testFavorite.getPriority());
        verify(favoriteRepository, times(1)).save(testFavorite);
    }

    @Test
    void testGetFavoritesForUser() {
        when(userxService.getCurrentUser()).thenReturn(testUser);
        when(favoriteRepository.findByUser(testUser)).thenReturn(List.of(testFavorite));

        List<Favorite> favorites = favoriteService.getFavoritesForUser();

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


}
