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

    List<StopEntity> findAllByNameContains(String preStopName);
}
