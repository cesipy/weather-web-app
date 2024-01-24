package at.qe.skeleton.external.repositories;

import at.qe.skeleton.external.model.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for managing {@link Location} entities in the database.
 * This interface extends the Spring JPA, so it provides basic CRUD operations.
 *
 * @see JpaRepository
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {


    List<Location> findByNameStartingWithIgnoreCase(String name);

    Location findFirstByNameStartingWithIgnoreCase(String name);

    Location findFirstByName(String name);


    // custom query not needed, as JPA implements queries automatically when naming is correctly done
    @Query("SELECT location FROM Location location WHERE name like %:keyword%")
    List<Location> search(@Param("keyword") String keyword);

    List<Location> findAll();
}