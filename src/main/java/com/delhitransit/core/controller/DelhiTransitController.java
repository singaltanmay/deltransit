package com.delhitransit.core.controller;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.service.RouteService;
import com.delhitransit.core.service.ShapePointService;
import com.delhitransit.core.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("delhitransit/v1")
public class DelhiTransitController {

    private final RouteService routeService;

    private final TripService tripService;

    private final ShapePointService shapePointService;

    @Autowired
    public DelhiTransitController(RouteService routeService, TripService tripService,
                                  ShapePointService shapePointService) {
        this.routeService = routeService;
        this.tripService = tripService;
        this.shapePointService = shapePointService;
    }

    @GetMapping("routes")
    public List<RouteEntity> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    @GetMapping("trips")
    public List<TripEntity> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("shapePoints")
    public List<ShapePointEntity> getAllShapePoints() {
        return shapePointService.getAllShapePoints();
    }
}
