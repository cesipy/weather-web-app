package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.FavoriteRepository;
import at.qe.skeleton.internal.services.UserxService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
public class FavoriteServiceIntegrationTest {
    public static Logger LOGGER = LoggerFactory.getLogger(FavoriteServiceIntegrationTest.class);
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserxService userxService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private FavoriteService favoriteService;
    private Userx userx;

    @AfterEach
    public void tearDown() {
        favoriteRepository.deleteAll();
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetFavorites() {
        Userx temp = userxService.getCurrentUser();
        LOGGER.info(String.valueOf(temp));

        String locationName = "Innsbruck";

        Location location = locationService.retrieveLocation(locationName);
        LOGGER.info(String.valueOf(location));

        Favorite favorite = new Favorite();
        favorite.setUser(temp);
        favorite.setLocation(location);
        favorite.setPriority(0);

        favoriteRepository.save(favorite);

        List<Favorite> favorites =   favoriteService.getFavorites(temp);
        LOGGER.info(favorites.toString());

        assertEquals(favorites.get(0).getLocation(), location);
    }

    @Test
    @WithMockUser(username = "user1", authorities =  {"EMPLOYEE"})
    public void testGetFavoritesNonAdmin() {
        Userx temp = userxService.getCurrentUser();
        LOGGER.info(String.valueOf(temp));

        String locationName = "Innsbruck";

        Location location = locationService.retrieveLocation(locationName);
        LOGGER.info(String.valueOf(location));

        Favorite favorite = new Favorite();
        favorite.setUser(temp);
        favorite.setLocation(location);
        favorite.setPriority(0);

        favoriteRepository.save(favorite);

        List<Favorite> favorites =   favoriteService.getFavorites(temp);
        LOGGER.info(favorites.toString());

        assertEquals(favorites.get(0).getLocation(), location);
    }

    @Test
    public void autocompleteTest() {
        String query = "absam";

        List<Location> locations = locationService.autocomplete(query);

        assertEquals(favoriteService.autocomplete(query).get(0).getId(), locations.get(0).getId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void saveFavoriteTest() {
        String query = "Vienna";
        favoriteService.saveFavorite(query);

        Location location =  locationService.retrieveLocation(query);
        LOGGER.info(String.valueOf(favoriteService.getCurrentUserx()));

        assertEquals(favoriteService.getCurrentUserx().getUsername(), "admin");
        assertEquals(favoriteService.getCurrentLocation(), location);
    }

    @Test
    @WithMockUser(username = "user1")
    public void isLocationAlreadyFavoriteTest() {
        String query1 = "Absam";
        Userx userx   = userxService.getCurrentUser();
        Location location = locationService.retrieveLocation(query1);

        Favorite favorite1 = new Favorite();
        favorite1.setPriority(1);
        favorite1.setLocation(location);
        favorite1.setUser(userx);

        favoriteRepository.save(favorite1);

        assertTrue(favoriteService.isLocationAlreadyFavorite(query1));

    }



}
