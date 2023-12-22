package at.qe.skeleton.external.repositories;

import at.qe.skeleton.external.model.location.LocationEntity;
import at.qe.skeleton.internal.repositories.AbstractRepository;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface LocationEntityRepository extends JpaRepository<LocationEntity, Integer> {


    @Query("SELECT name FROM LocationEntity WHERE name like %:keyword%")
    public List<String> search(@Param("keyword") String keyword);

}
