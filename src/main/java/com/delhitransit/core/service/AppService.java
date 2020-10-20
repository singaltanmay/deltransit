package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class AppService {

    private final RouteService routeService;

    private final ShapePointService shapePointService;

    private final StopService stopService;

    private final StopTimeService stopTimeService;

    private final TripService tripService;

    @Autowired
    public AppService(RouteService routeService, ShapePointService shapePointService,
                      StopService stopService, StopTimeService stopTimeService,
                      TripService tripService) {
        this.routeService = routeService;
        this.shapePointService = shapePointService;
        this.stopService = stopService;
        this.stopTimeService = stopTimeService;
        this.tripService = tripService;
    }

    public List<RouteEntity> routesBetweenTwoStops(long stop1, long stop2) {
        List<RouteEntity> routes = new LinkedList<>();
        List<StopTimeEntity> stopTimeEntities = stopTimeService.getAllStopTimesByStopId(stop1);
        for (StopTimeEntity stopTimeEntity : stopTimeEntities) {
            TripEntity trip = stopTimeEntity.getTrip();
            for (StopTimeEntity stopTime : trip.getStopTimes()) {
                if (stopTime.getStop().getStopId() == stop2) {
                    routes.add(trip.getRoute());
                }
            }
        }
        return routes;
    }
}
