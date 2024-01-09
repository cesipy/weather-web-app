package at.qe.skeleton.external.services;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.Favorite;
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
    public List<Favorite> getFavorites(Userx userx) {
        return favoriteRepository.findByUser(userx);
    }
    private Userx currentUserx;
    private Location currentLocation;

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

        return getFavorites(userx);
    }

    public List<Location> autocomplete(String query) {
        return locationService.autocomplete(query);
    }

    public boolean isLocationAlreadyFavorite(String locationName) {
        retrieveCurrentData(locationName);

        List<Favorite> favorites = favoriteRepository.findByUser(currentUserx);
        if (!favorites.isEmpty()) {
            // check if location is already saved

            return favorites.stream()
                    .anyMatch(favorite -> favorite.getLocation().equals(currentLocation));
        }
        return false;
    }

    private void retrieveCurrentData(String locationName) {
        currentUserx = userxService.getCurrentUser();

        currentLocation = locationService.retrieveLocation(locationName);
    }

    // maybe with Favorite instance, instead of creation
    public void saveFavorite( String locationName) {
        retrieveCurrentData(locationName);

        if (currentLocation == null) {
            // TODO: proper handling
            return;
        }

        int priority = calculatePriority(currentUserx);
        LOGGER.info(String.valueOf(currentUserx));

        Favorite favorite = new Favorite();
        favorite.setUser(currentUserx);
        favorite.setLocation(currentLocation);
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
        return favorites.size() + 1;
    }

    // TODO: implement updateFavorite method

    public Userx getCurrentUserx() {
        return currentUserx;
    }

    public void setCurrentUserx(Userx currentUserx) {
        this.currentUserx = currentUserx;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

}
