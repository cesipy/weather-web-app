package at.qe.skeleton.internal.services;

import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.internal.model.Favorite;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    FavoriteRepository favoriteRepository;

    List<Favorite> favorites;

    public List<Favorite> getFavorites(Userx userx) {
        return favoriteRepository.findByUser(userx);
    }


    public List<Favorite> getFavoritesByUsername(String username) {
        return favoriteRepository.findByUserUsername(username);
    }

    // maybe with Favorite instance, instead of creation
    public void saveFavorite(Userx userx, Location location, int priority) {
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

}
