/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.repository;

import com.delhitransit.core.model.entity.TripEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<TripEntity, Long> {

    Page<TripEntity> findAll(Pageable request);

}
