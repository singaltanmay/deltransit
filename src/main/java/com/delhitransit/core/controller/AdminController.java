package com.delhitransit.core.controller;

import com.delhitransit.core.service.RouteService;
import com.delhitransit.core.service.ShapePointService;
import com.delhitransit.core.service.StopService;
import com.delhitransit.core.service.StopTimeService;
import com.delhitransit.core.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("v1/admin")
public class AdminController {

    private final RouteService routeService;

    private final TripService tripService;

    private final ShapePointService shapePointService;

    private final StopService stopService;

    private final StopTimeService stopTimeService;

    @Autowired
    public AdminController(RouteService routeService, TripService tripService, ShapePointService shapePointService,
                           StopService stopService, StopTimeService stopTimeService) {
        this.routeService = routeService;
        this.tripService = tripService;
        this.shapePointService = shapePointService;
        this.stopService = stopService;
        this.stopTimeService = stopTimeService;
    }

    @PostMapping("init/routes")
    public void initializeRoutesTable() throws IOException {
        routeService.initializeUnlinkedDatabase();
    }

    @PostMapping("init/trips")
    public void initializeTripsTable() throws IOException {
        tripService.initializeUnlinkedDatabase();
    }

    @PostMapping("init/shapePoints")
    public void initializeShapePointsTable() throws IOException {
        shapePointService.initializeUnlinkedDatabase();
    }

    @PostMapping("init/stops")
    public void initializeStopsTable() throws IOException {
        stopService.initializeUnlinkedDatabase();
    }

    @PostMapping("init/stopTimes")
    public void initializeStopTimesTable() throws IOException {
        stopTimeService.initializeUnlinkedDatabase();
    }
}
