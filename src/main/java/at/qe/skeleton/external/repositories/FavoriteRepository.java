package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.internal.model.Userx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(Userx userx);

    List<Favorite> findByUserUsername(String username);

    List<Favorite> findByUserAndPriority(Userx userx, int priority);
}
