/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.repository;

import com.delhitransit.core.model.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

    List<RouteEntity> findAllByRouteId(long routeId);

}
