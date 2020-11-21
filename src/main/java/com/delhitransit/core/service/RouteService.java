/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

    RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    private RouteEntity removeTripsFromRoute(RouteEntity route) {
        if (route != null) {
            route.setTrips(null);
        }
        return route;
    }

    public Page<RouteEntity> getAllRoutes(Pageable request) {
        Page<RouteEntity> page = routeRepository.findAll(request);
        page.forEach(this::removeTripsFromRoute);
        return page;
    }

    public Page<RouteEntity> getRoutesByRouteId(long routeId, Pageable request) {
        Page<RouteEntity> page = routeRepository.findAllByRouteId(routeId, request);
        page.forEach(this::removeTripsFromRoute);
        return page;
    }

    public Page<RouteEntity> getRoutesByShortNameIgnoreCase(String name, Pageable request) {
        Page<RouteEntity> page = routeRepository.findAllByShortNameIgnoreCase(name, request);
        page.forEach(this::removeTripsFromRoute);
        return page;
    }

    public Page<RouteEntity> getRoutesByShortNameContainsIgnoreCase(String name, Pageable request) {
        Page<RouteEntity> page = routeRepository.findAllByShortNameContainsIgnoreCase(name, request);
        page.forEach(this::removeTripsFromRoute);
        return page;
    }

    public Page<RouteEntity> getRoutesByLongNameIgnoreCase(String name, Pageable request) {
        Page<RouteEntity> page = routeRepository.findAllByLongNameIgnoreCase(name, request);
        page.forEach(this::removeTripsFromRoute);
        return page;
    }

    public Page<RouteEntity> getRoutesByLongNameContainsIgnoreCase(String name, Pageable request) {
        Page<RouteEntity> page = routeRepository.findAllByLongNameContainsIgnoreCase(name, request);
        page.forEach(this::removeTripsFromRoute);
        return page;
    }

    public Page<RouteEntity> getRoutesByType(int type, Pageable request) {
        RouteEntity.ROUTE_TYPE routeType = RouteEntity.getRouteType(type);
        return getRoutesByType(routeType, request);
    }

    public Page<RouteEntity> getRoutesByType(RouteEntity.ROUTE_TYPE type, Pageable request) {
        Page<RouteEntity> page = routeRepository.findAllByType(type, request);
        page.forEach(this::removeTripsFromRoute);
        return page;
    }

}
