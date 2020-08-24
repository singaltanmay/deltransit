/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<RouteEntity> getAllRoutes() {
        return removeTripsFromRoutes(routeRepository.findAll());
    }

    public List<RouteEntity> getRouteByRouteId(long routeId) {
        return removeTripsFromRoutes(routeRepository.findAllByRouteId(routeId));
    }

    private List<RouteEntity> removeTripsFromRoutes(List<RouteEntity> routes) {
        if (routes != null && routes.size() > 0) {
            for (RouteEntity route : routes) {
                route.setTrips(null);
            }
        }
        return routes;
    }

    private void insertRoutes(List<RouteEntity> routes) {
        if (routes != null && routes.size() > 0) {
            routeRepository.saveAll(routes);
        }
    }

}
