/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.repository;

import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.parseable.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<StopEntity, Long> {

    List<StopEntity> findStopEntitiesByNameStartingWith(String preStopName);
}
