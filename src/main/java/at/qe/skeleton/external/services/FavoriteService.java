package at.qe.skeleton.external.services;

import at.qe.skeleton.external.controllers.EmptyLocationException;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.internal.model.Favorite;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.FavoriteRepository;
import at.qe.skeleton.internal.services.UserxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope("application")
public class FavoriteService {

    private static Logger LOGGER = LoggerFactory.getLogger(FavoriteService.class);
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserxService userxService;
    @Autowired
    private LocationService locationService;
    private List<Favorite> favorites;
    public List<Favorite> getFavorites(Userx userx) {
        return favoriteRepository.findByUser(userx);
    }

    public void updateFavoritePriority(Long id, int priority) {
        Optional<Favorite> favoriteQuery = favoriteRepository.findById(id);

        if (favoriteQuery.isPresent()) {
            Favorite favorite = favoriteQuery.get();
            favorite.setPriority(priority);
            favoriteRepository.save(favorite);
        }
        else {
            LOGGER.info("favorite with id " + id + " not found!");
        }
    }

    public List<Favorite> getFavoritesForUser() {
        Userx userx = userxService.getCurrentUser();
        LOGGER.info("username: " + userx);

        return favoriteRepository.findByUser(userx);
    }

    public List<Location> autocomplete(String query) {
        return locationService.autocomplete(query);
    }

    // maybe with Favorite instance, instead of creation
    public void saveFavorite( String locationName) throws EmptyLocationException {

        Userx userx = userxService.getCurrentUser();
        LOGGER.info(String.valueOf(userx));
        Location location = locationService.retrieveLocation(locationName);

        if (location == null) {
            // TODO: proper handling
            return;
        }
        int priority = calculatePriority(userx);

        Favorite favorite = new Favorite();
        favorite.setUser(userx);
        favorite.setLocation(location);
        favorite.setPriority(priority);

        favoriteRepository.save(favorite);
    }


    public void deleteFavorite(Favorite favorite)
    {
        favoriteRepository.delete(favorite);
    }

    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }

    private int calculatePriority(Userx userx) {
        List<Favorite> favorites =  favoriteRepository.findByUser(userx);

        LOGGER.info("priority: " + favorites.size());
        return favorites.size();
    }

    // TODO: implement updateFavorite method

}
