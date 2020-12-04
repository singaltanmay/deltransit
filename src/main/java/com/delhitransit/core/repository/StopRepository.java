/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.repository;

import com.delhitransit.core.model.entity.StopEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StopRepository extends JpaRepository<StopEntity, Long> {

    Page<StopEntity> findAll(Pageable request);

    Page<StopEntity> findAllByNameIgnoreCase(String name, Pageable request);

    Page<StopEntity> findAllByNameContainsIgnoreCase(String preStopName, Pageable request);

    Optional<StopEntity> findFirstByStopId(long stopId);

    Page<StopEntity> findAllByLatitudeBetweenAndLongitudeBetween(
            double lat1, double lat2, double lon1, double lon2, Pageable request);

}
