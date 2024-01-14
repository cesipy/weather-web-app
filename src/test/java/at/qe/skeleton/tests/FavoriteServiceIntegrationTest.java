package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.services.FavoriteService;
import at.qe.skeleton.external.services.LocationService;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.external.repositories.FavoriteRepository;
import at.qe.skeleton.internal.services.UserxService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

        List<Favorite> favorites =   favoriteService.getSortedFavoritesList(temp);
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

        List<Favorite> favorites =   favoriteService.getSortedFavoritesList(temp);
        LOGGER.info(favorites.toString());

        assertEquals(favorites.get(0).getLocation(), location);
    }

    @Test
    public void testAutocomplete() {
        String query = "absam";

        List<Location> locations = locationService.autocomplete(query);

        assertEquals(favoriteService.autocomplete(query).get(0).getId(), locations.get(0).getId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testSaveFavorite() {
        String query = "Vienna";
        favoriteService.saveFavorite(query);

        Location location =  locationService.retrieveLocation(query);
        LOGGER.info(String.valueOf(favoriteService.getCurrentUserx()));

        assertEquals(favoriteService.getCurrentUserx().getUsername(), "admin");
        assertEquals(favoriteService.getCurrentLocation(), location);
    }

    @Test
    @WithMockUser(username = "user1")
    public void testIsLocationAlreadyFavorite() {
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

    @Test
    @WithMockUser(username = "user1")
    public void testGetSortedFavoritesList() {
        userx = userxService.getCurrentUser();
        String query1 = "Innsbruck";
        String query2 = "Vienna";
        String query3 = "Salzburg";

        createAndSaveFavorite(query1, 1, userx);
        createAndSaveFavorite(query2, 2, userx);
        createAndSaveFavorite(query3, 3, userx);

        List <Favorite> favorites = favoriteService.getSortedFavoritesList(userx);
        LOGGER.info(favorites.toString());

        assertEquals(favorites.get(0).getLocation().getName(), query1);
        assertEquals(favorites.get(0).getPriority(), 1);
        assertEquals(favorites.get(1).getLocation().getName(), query2);
        assertEquals(favorites.get(1).getPriority(), 2);
        assertEquals(favorites.get(2).getLocation().getName(), query3);
        assertEquals(favorites.get(2).getPriority(), 3);
        assertEquals(favorites.size(), 3);
    }


    @Test
    @WithMockUser(username = "admin")
    public void testMoveFavoriteUp() {
        userx = userxService.getCurrentUser();

        String query1 = "Absam";
        String query2 = "Hall in Tirol";

        favoriteService.saveFavorite(query1);
        favoriteService.saveFavorite(query2);

        List<Favorite> favorites = favoriteService.getSortedFavoritesList(userx);
        Favorite favorite1 = favorites.get(0);
        Favorite favorite2 = favorites.get(1);

        favoriteService.moveFavoriteUpOrDown(favorite2, true);

        favorites = favoriteService.getSortedFavoritesList(userx);
        LOGGER.info(favorites.toString());
        assertEquals(favorites.get(0).getLocation().getName(), query2);
        assertEquals(favorites.get(0).getId(), favorite2.getId());

        assertEquals(favorites.get(1).getLocation().getName(), query1);
        assertEquals(favorites.get(1).getId(), favorite1.getId());
    }


    @Test
    @WithMockUser(username = "admin")
    public void testMoveFavoriteDown() {
        userx = userxService.getCurrentUser();

        String query1 = "Absam";
        String query2 = "Hall in Tirol";

        favoriteService.saveFavorite(query1);
        favoriteService.saveFavorite(query2);

        List<Favorite> favorites = favoriteService.getSortedFavoritesList(userx);
        Favorite favorite1 = favorites.get(0);
        Favorite favorite2 = favorites.get(1);

        favoriteService.moveFavoriteUpOrDown(favorite1, false);

        favorites = favoriteService.getSortedFavoritesList(userx);
        LOGGER.info(favorites.toString());
        assertEquals(favorites.get(0).getLocation().getName(), query2);
        assertEquals(favorites.get(0).getId(), favorite2.getId());

        assertEquals(favorites.get(1).getLocation().getName(), query1);
        assertEquals(favorites.get(1).getId(), favorite1.getId());
    }

    public Favorite createAndSaveFavorite(String locationName, int priority, Userx userx) {
        Location location = locationService.retrieveLocation(locationName);

        Favorite favorite = new Favorite();
        favorite.setLocation(location);
        favorite.setUser(userx);
        favorite.setPriority(priority);
        favoriteRepository.save(favorite);

        return favorite;
    }

}
