package com.delhitransit.core.api.controller;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.service.AppService;
import com.delhitransit.core.service.RouteService;
import com.delhitransit.core.service.ShapePointService;
import com.delhitransit.core.service.StopService;
import com.delhitransit.core.service.StopTimeService;
import com.delhitransit.core.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1")
public class DelhiTransitController {

    private final RouteService routeService;

    private final TripService tripService;

    private final ShapePointService shapePointService;

    private final StopService stopService;

    private final StopTimeService stopTimeService;

    private final AppService appService;

    @Autowired
    public DelhiTransitController(RouteService routeService, TripService tripService,
                                  ShapePointService shapePointService, StopService stopService,
                                  StopTimeService stopTimeService, AppService appService) {
        this.routeService = routeService;
        this.tripService = tripService;
        this.shapePointService = shapePointService;
        this.stopService = stopService;
        this.stopTimeService = stopTimeService;
        this.appService = appService;
    }

    @GetMapping("routes")
    public List<RouteEntity> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    @GetMapping("routes/id/{id}")
    public List<RouteEntity> getRoutesByRouteId(@PathVariable("id") long routeId) {
        return routeService.getRoutesByRouteId(routeId);
    }

    @GetMapping("routes/name/{name}")
    public List<RouteEntity> getRoutesByRouteName(
            @PathVariable("name") String name,
            @RequestParam(name = "exact") Optional<Boolean> matchType,
            @RequestParam(name = "short") Optional<Boolean> nameType) {
        boolean isExactMatch = matchType != null && matchType.isPresent() && matchType.get();
        boolean isShortName = nameType != null && nameType.isPresent() && nameType.get();
        if (isShortName) {
            if (isExactMatch) {
                return routeService.getRoutesByShortNameIgnoreCase(name);
            } else {
                return routeService.getRoutesByShortNameContainsIgnoreCase(name);
            }
        } else {
            if (isExactMatch) {
                return routeService.getRoutesByLongNameIgnoreCase(name);
            } else {
                return routeService.getRoutesByLongNameContainsIgnoreCase(name);
            }
        }
    }

    @GetMapping("routes/type/{type}")
    public List<RouteEntity> getRoutesByRouteType(@PathVariable("type") int type) {
        return routeService.getRoutesByType(type);
    }

    @GetMapping("routes/between")
    public List<RouteEntity> getRoutesBetweenStops(@RequestParam long source, @RequestParam long destination) {
        return appService.getRoutesBetweenTwoStops(source, destination);
    }

    @GetMapping("trips")
    public List<TripEntity> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("shapePoints")
    public List<ShapePointEntity> getAllShapePoints() {
        return shapePointService.getAllShapePoints();
    }

    @GetMapping("stops")
    public List<StopEntity> getAllStops() {
        return stopService.getAllStops();
    }

    @GetMapping("stops/name/{name}")
    public List<StopEntity> getStopsByName(
            @PathVariable String name,
            @RequestParam(name = "exact") Optional<Boolean> matchType) {
        boolean isExactMatch = matchType != null && matchType.isPresent() && matchType.get();
        if (isExactMatch) {
            return stopService.getStopsByNameIgnoreCase(name);
        } else {
            return stopService.getStopsByNameContainsIgnoreCase(name);
        }
    }


    @GetMapping("stopTimes")
    public List<StopTimeEntity> getAllStopTimes() {
        return stopTimeService.getAllStopTimes();
    }

}
