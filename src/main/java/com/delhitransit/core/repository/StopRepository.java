/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.repository;

import com.delhitransit.core.model.entity.StopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<StopEntity, Long> {

    List<StopEntity> findAllByNameIgnoreCase(String name);

    List<StopEntity> findAllByNameContainsIgnoreCase(String preStopName);

    List<StopEntity> findAllByLatitudeBetweenAndLongitudeBetween(double lat1, double lat2, double lon1, double lon2);
}
