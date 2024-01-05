package at.qe.skeleton.external.services;

import at.qe.skeleton.external.controllers.LocationControllerDb;
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


    public List<Favorite> getFavoritesForUser() {
        Userx userx = userxService.getCurrentUser();
        LOGGER.info("username: " + userx);

        return favoriteRepository.findByUser(userx);

    }

    // maybe with Favorite instance, instead of creation
    public void saveFavorite( String locationName, int priority) {

        Userx userx = userxService.getCurrentUser();
        LOGGER.info(String.valueOf(userx));
        Location location = locationService.retrieveLocation(locationName);

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

    // TODO: implement updateFavorite method

}
