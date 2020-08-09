package com.delhitransit.core.controller;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.model.parseable.Route;
import com.delhitransit.core.service.RouteService;
import com.delhitransit.core.service.TripService;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("delhitransit/v1")
public class DelhiTransitController {

    private RouteService routeService;
    private TripService tripService;

    @Autowired
    public DelhiTransitController(RouteService routeService) {
        this.routeService = routeService;
        this.tripService = tripService;
    }

    @GetMapping("routes")
    public List<RouteEntity> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    @GetMapping("trips")
    public List<TripEntity> getAllTrips() {
        return tripService.getAllTrips();
    }
}
