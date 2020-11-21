/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.repository;

import com.delhitransit.core.model.entity.RouteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

    Page<RouteEntity> findAll(Pageable request);

    Page<RouteEntity> findAllByRouteId(long routeId, Pageable request);

    Page<RouteEntity> findAllByShortNameIgnoreCase(String name, Pageable request);

    Page<RouteEntity> findAllByShortNameContainsIgnoreCase(String name, Pageable request);

    Page<RouteEntity> findAllByLongNameIgnoreCase(String name, Pageable request);

    Page<RouteEntity> findAllByLongNameContainsIgnoreCase(String name, Pageable request);

    Page<RouteEntity> findAllByType(RouteEntity.ROUTE_TYPE type, Pageable request);

}
