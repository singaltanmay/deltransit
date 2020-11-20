package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        HashSet<RouteEntity> routes = new HashSet<>();
        List<StopTimeEntity> sourceStopTimes = stopTimeService.getAllStopTimesByStopId(sourceStopId);
        for (StopTimeEntity sourceStopTime : sourceStopTimes) {
            TripEntity tripContainingBothStops = sourceStopTime.getTrip();
            for (StopTimeEntity destinationStopTime : tripContainingBothStops.getStopTimes()) {
                if (destinationStopTime.getStop().getStopId() == destinationStopId &&
                        destinationStopTime.getStopSequence() > sourceStopTime.getStopSequence()) {
                    routes.add(tripContainingBothStops.getRoute());
                }
            }
        }
        return routes.parallelStream().collect(Collectors.toList());
    }

    public List<RouteEntity> getRoutesBetweenTwoStopsNewImplementation(long sourceStopId, long destinationStopId) {
        List<StopTimeEntity> sourceStopTimes = stopTimeService.getAllStopTimesByStopId(sourceStopId);
        List<StopTimeEntity> destinationStopTimes = stopTimeService.getAllStopTimesByStopId(destinationStopId);

        HashSet<String> sourceTrips = new HashSet<>();
        for(StopTimeEntity stopTime : sourceStopTimes){
            sourceTrips.add(stopTime.getTrip().getTripId());
        }

        HashSet<TripEntity> candidateTrips = new HashSet<>();
        for(StopTimeEntity stopTime : destinationStopTimes){
            TripEntity trip = stopTime.getTrip();
            if (sourceTrips.contains(trip.getTripId())){
                candidateTrips.add(trip);
            }
        }

        HashSet<RouteEntity> routes = new HashSet<>();
        for(TripEntity trip : candidateTrips){
            routes.add(trip.getRoute());
        }
        return routes.parallelStream().collect(Collectors.toList());
    }

}
