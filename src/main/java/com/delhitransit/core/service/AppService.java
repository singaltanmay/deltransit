package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<RouteEntity> getRoutesBetweenTwoStops(long sourceStopId, long destinationStopId) {
        List<StopTimeEntity> sourceStopTimes = stopTimeService.getAllStopTimesByStopId(sourceStopId);
        List<StopTimeEntity> destinationStopTimes = stopTimeService.getAllStopTimesByStopId(destinationStopId);

        HashMap<String, Long> sourceTrips = new HashMap<>();
        for (StopTimeEntity stopTime : sourceStopTimes) {
            sourceTrips.putIfAbsent(stopTime.getTrip().getTripId(), stopTime.getArrival());
        }

        HashSet<TripEntity> candidateTrips = new HashSet<>();
        for (StopTimeEntity stopTime : destinationStopTimes) {
            TripEntity trip = stopTime.getTrip();
            if (sourceTrips.containsKey(trip.getTripId()) &&
                    sourceTrips.get(trip.getTripId()) <= stopTime.getArrival()) {
                candidateTrips.add(trip);
            }
        }

        HashSet<RouteEntity> routes = new HashSet<>();
        candidateTrips.forEach(it -> routes.add(it.getRoute()));
        return routes.parallelStream().collect(Collectors.toList());
    }

    public List<ShapePointEntity> getShapePointsByTripId(String tripId) {
        TripEntity trip = tripService.getTripByTripId(tripId);
        return trip != null ? trip.getShapePoints() : Collections.emptyList();
    }

}
