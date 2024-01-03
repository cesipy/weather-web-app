package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.Favorite;
import at.qe.skeleton.internal.model.Userx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository {
    List<Favorite> findByUser(Userx userx);

}
