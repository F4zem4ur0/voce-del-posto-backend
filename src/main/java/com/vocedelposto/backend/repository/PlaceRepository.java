package com.vocedelposto.backend.repository;

import com.vocedelposto.backend.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCategory(String category);

    @Query(value = """
        SELECT * FROM places p
        WHERE (6371 * acos(
            cos(radians(:lat)) * cos(radians(p.latitude)) *
            cos(radians(p.longitude) - radians(:lon)) +
            sin(radians(:lat)) * sin(radians(p.latitude))
        )) <= :radiusKm
        """, nativeQuery = true)
    List<Place> findNearby(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("radiusKm") double radiusKm
    );
}