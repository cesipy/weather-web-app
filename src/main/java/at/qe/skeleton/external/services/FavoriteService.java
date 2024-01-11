package at.qe.skeleton.external.services;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.external.repositories.FavoriteRepository;
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
    private Userx currentUserx;
    private Location currentLocation;


    /**
     * Moves a favorite location up or down in priority.
     *
     * @param favorite The favorite location to be moved.
     * @param up       A boolean indicating whether to move up (true) or down (false).
     */
    public void moveFavoriteUpOrDown(Favorite favorite, boolean up) {
        Userx userx = getCurrentUserx();
        int oldPriority = favorite.getPriority();
        int newPriority = (up) ? oldPriority - 1 : oldPriority + 1;
        int currentMaxPriority = calculatePriority(userx);

        if (oldPriority <= 1 && up) {
            LOGGER.info("Cant move Location up!" + favorite);
            return;
        } else if (oldPriority >= currentMaxPriority && !up) {
            LOGGER.info("Cant move Location down!" + favorite);
            return;
        }

        Optional<Favorite> favoriteToMoveOptional = favoriteRepository.findFirstByUserAndPriority(userx, newPriority);
        if (favoriteToMoveOptional.isPresent()) {
            Favorite favoriteToMove = favoriteToMoveOptional.get();

            favoriteToMove.setPriority(oldPriority);
            favoriteRepository.save(favoriteToMove);

            favorite.setPriority(newPriority);
            favoriteRepository.save(favorite);

            LOGGER.info("after successful move, og. favorite priority: " + favorite.getPriority() + "other: " + favoriteToMove.getPriority());
        }
        else {
            LOGGER.info("Error fetching favorites");
            // TODO: Proper exception
        }
    }

    /**
     * Updates the priority of a favorite location.
     *
     * @param favorite    The favorite location to be updated.
     * @param newPriority The new priority value.
     */
    public void updateFavoritePriority(Favorite favorite, int newPriority) {
        // currently not in use
        if (newPriority < 0) {
            LOGGER.info("Priority is too small: " + newPriority);
            return;
        }
        int oldPriority = favorite.getPriority();
        LOGGER.info("in updatePriority with arguments: " + favorite + "; newPrio: " + newPriority + "; oldPrio: " + oldPriority);

        // get current max priority for user
        currentUserx = userxService.getCurrentUser();
        int currentMaxPriority = calculatePriority(currentUserx);
        List<Favorite>  favoritesByPriority = favoriteRepository.findByUserAndPriority(currentUserx, newPriority);
        LOGGER.info(" favorites with this priority" + favoritesByPriority.toString());

        boolean isPriorityAlreadyTaken = !favoritesByPriority.isEmpty();

        LOGGER.info("currentMaxPriority for user "+ currentUserx  + ": " + currentMaxPriority);

        if (newPriority > (currentMaxPriority)) {
            LOGGER.info("priority" + newPriority + " is too high");
            return;
        }
        if (isPriorityAlreadyTaken) {
            LOGGER.info("favorite priority" + newPriority + " already in use, switching!");
            rebasePrioritiesFromPriority(newPriority, oldPriority);
        }

        favorite.setPriority(newPriority);      // update priority of favorite
        favoriteRepository.save(favorite);

        LOGGER.info("Successfully updated priority for favorite with ID: " + favorite);
        LOGGER.info("Updated priority is now: " + newPriority);
    }

    /**
     * Rebases the priorities of favorites starting from a specific priority to another priority.
     *
     * @param startingPriority The starting priority.
     * @param endingPriority   The ending priority.
     */
    public void rebasePrioritiesFromPriority(int startingPriority, int endingPriority) {
        // currently not in use
        List<Favorite> favorites = favoriteRepository.findByUser(currentUserx);

        // for all favorites with priority >= starting priority, all priorities are
        // increased by one.
        for (Favorite favorite : favorites) {
            int priority = favorite.getPriority();
            if (priority >= startingPriority && priority < endingPriority) {
                favorite.setPriority(priority + 1);
                favoriteRepository.save(favorite);
                LOGGER.info("updated priority " + priority + " to " + (priority+1));
            }
        }
    }

    /**
     * Retrieves a sorted list of favorite locations for the currently logged-in user.
     *
     * @return A sorted list of favorite locations associated with the current user.
     */
    public List<Favorite> getSortedFavoritesForUser() {
        Userx userx = userxService.getCurrentUser();

        return getSortedFavoritesList(userx);
    }

    /**
     * Retrieves a sorted list of favorite locations for a specific user based on priority.
     *
     * @param userx The user for whom to retrieve and sort favorite locations.
     * @return A sorted list of favorite locations associated with the user.
     */
    public List<Favorite> getSortedFavoritesList(Userx userx) {
        return favoriteRepository.findByUserOrderByPriority(userx);
    }

    /**
     * Autocompletes location names based on the provided query. This method is a
     * minimal implementation of LocationController's autocomplete
     *
     * @param query The query to autocomplete.
     * @return A list
     */
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


    /**
     * Retrieves current user and location data based on the provided location name.
     *
     * @param locationName The name of the location to retrieve data for.
     */
    private void retrieveCurrentData(String locationName) {
        currentUserx = userxService.getCurrentUser();
        currentLocation = locationService.retrieveLocation(locationName);
    }

    /**
     * Saves a new favorite location for the current user.
     *
     * @param locationName The name of the location to be saved as a favorite.
     */
    public void saveFavorite( String locationName) {
        retrieveCurrentData(locationName);
        if (currentLocation == null) {
            // TODO: proper handling
            return;
        }
        int priority = calculatePriority(currentUserx) + 1;

        Favorite favorite = new Favorite();
        favorite.setUser(currentUserx);
        favorite.setLocation(currentLocation);
        favorite.setPriority(priority);
        favoriteRepository.save(favorite);

        LOGGER.info("successfully saved favorite " + favorite + " for " + currentUserx);
    }

    /**
     * Deletes the specified favorite from the repository.
     *
     * @param favorite The favorite to be deleted.
     */
    public void deleteFavorite(Favorite favorite)
    {
        favoriteRepository.delete(favorite);
    }


    /**
     * Deletes a favorite by its unique identifier from the repository.
     *
     * @param id The unique identifier of the favorite to be deleted.
     */
    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }


    /**
     * Calculates and returns the priority count for a given user.
     *
     * @param userx The user for whom the priority count is calculated.
     * @return The calculated priority count based on the number of favorites for the user.
     */
    private int calculatePriority(Userx userx) {
        List<Favorite> favorites =  favoriteRepository.findByUser(userx);
        return favorites.size();
    }


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
