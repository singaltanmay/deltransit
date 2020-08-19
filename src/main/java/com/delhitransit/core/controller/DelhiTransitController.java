package com.delhitransit.core.controller;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.service.RouteService;
import com.delhitransit.core.service.ShapePointService;
import com.delhitransit.core.service.StopService;
import com.delhitransit.core.service.StopTimeService;
import com.delhitransit.core.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1")
public class DelhiTransitController {

    private final RouteService routeService;

    private final TripService tripService;

    private final ShapePointService shapePointService;

    private final StopService stopService;

    private final StopTimeService stopTimeService;

    @Autowired
    public DelhiTransitController(RouteService routeService, TripService tripService,
                                  ShapePointService shapePointService, StopService stopService,
                                  StopTimeService stopTimeService) {
        this.routeService = routeService;
        this.tripService = tripService;
        this.shapePointService = shapePointService;
        this.stopService = stopService;
        this.stopTimeService = stopTimeService;
    }

    @GetMapping("routes")
    public List<RouteEntity> getAllRoutes() {
        return routeService.getAllRoutes().subList(0,19);
    }

    @GetMapping("trips")
    public List<TripEntity> getAllTrips() {
        return tripService.getAllTrips().subList(0,19);
    }

    @GetMapping("shapePoints")
    public List<ShapePointEntity> getAllShapePoints() {
        return shapePointService.getAllShapePoints().subList(0,19);
    }

    @GetMapping("stops")
    public List<StopEntity> getAllStops() {
        return stopService.getAllStops().subList(0,19);
    }

    @GetMapping("stopTimes")
    public List<StopTimeEntity> getAllStopTimes() {
        return stopTimeService.getAllStopTimes().subList(0,19);
    }

}
