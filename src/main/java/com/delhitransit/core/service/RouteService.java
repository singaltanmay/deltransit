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

import java.util.List;

@Service
public class RouteService {

    RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    static List<RouteEntity> removeTripsFromRoutes(List<RouteEntity> routes) {
        if (routes != null && routes.size() > 0) {
            for (RouteEntity route : routes) {
                route.setTrips(null);
            }
        }
        return routes;
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

    public List<RouteEntity> getRoutesByRouteId(long routeId) {
        return removeTripsFromRoutes(routeRepository.findAllByRouteId(routeId));
    }

    public List<RouteEntity> getRoutesByShortNameIgnoreCase(String name) {
        return removeTripsFromRoutes(routeRepository.findAllByShortNameIgnoreCase(name));
    }

    public List<RouteEntity> getRoutesByShortNameContainsIgnoreCase(String name) {
        return removeTripsFromRoutes(routeRepository.findAllByShortNameContainsIgnoreCase(name));
    }

    public List<RouteEntity> getRoutesByLongNameIgnoreCase(String name) {
        return removeTripsFromRoutes(routeRepository.findAllByLongNameIgnoreCase(name));
    }

    public List<RouteEntity> getRoutesByLongNameContainsIgnoreCase(String name) {
        return removeTripsFromRoutes(routeRepository.findAllByLongNameContainsIgnoreCase(name));
    }

    public List<RouteEntity> getRoutesByType(int type) {
        RouteEntity.ROUTE_TYPE routeType = RouteEntity.getRouteType(type);
        return getRoutesByType(routeType);
    }

    public List<RouteEntity> getRoutesByType(RouteEntity.ROUTE_TYPE type) {
        return removeTripsFromRoutes(routeRepository.findAllByType(type));
    }

    private void insertRoutes(List<RouteEntity> routes) {
        if (routes != null && routes.size() > 0) {
            routeRepository.saveAll(routes);
        }
    }

}
