/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.repository;

import com.delhitransit.core.model.entity.StopTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopTimeRepository extends JpaRepository<StopTimeEntity, Long> {

    List<StopTimeEntity> findAllByStop_StopId(long stopId);

}
