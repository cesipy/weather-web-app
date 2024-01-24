package at.qe.skeleton.external.repositories;

import at.qe.skeleton.external.model.Favorite;
import at.qe.skeleton.internal.model.Userx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing favorite locations in the database.
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(Userx userx);

    List<Favorite> findByUserOrderByPriority(Userx userx);

    List<Favorite> findByUserUsername(String username);

    List<Favorite> findByUserAndPriority(Userx userx, int priority);

    Optional<Favorite> findFirstByUserAndPriority(Userx userx, int priority);
}