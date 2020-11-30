package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.model.response.ResponseRoutesBetween;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

@Service
public class AppService {

    private final RouteService routeService;

    private final ShapePointService shapePointService;

    private final StopService stopService;

    private final StopTimeService stopTimeService;

    private final TripService tripService;

    @Getter
    private final ClientQueries clientQueries;

    @Autowired
    public AppService(RouteService routeService, ShapePointService shapePointService,
                      StopService stopService, StopTimeService stopTimeService,
                      TripService tripService) {
        this.routeService = routeService;
        this.shapePointService = shapePointService;
        this.stopService = stopService;
        this.stopTimeService = stopTimeService;
        this.tripService = tripService;
        this.clientQueries = new ClientQueries();
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
        List<ShapePointEntity> entities = trip != null ? trip.getShapePoints() : Collections.emptyList();
        ShapePointService.sortShapePointsBySequence(entities);
        return entities;
    }

    public List<RouteEntity> getRoutesByStopIdAndStopTimeArrivalTime(long stopId, long arrivalTime) {
        List<StopTimeEntity> stopTimes = stopTimeService
                .getAllStopTimesByStopIdAndArrivalTimeAfter(stopId, arrivalTime);
        HashSet<RouteEntity> routes = new HashSet<>();
        for (StopTimeEntity stopTime : stopTimes) {
            RouteEntity route = stopTime.getTrip().getRoute();
            route.setTrips(null);
            routes.add(route);
        }
        return new LinkedList<>(routes);
    }

    public List<StopEntity> getStopsByTripId(String tripId) {
        TripEntity trip = tripService.getTripByTripId(tripId);
        List<StopTimeEntity> stopTimes = trip.getStopTimes();
        StopTimeService.sortStopTimesByStopArrivalTime(stopTimes);
        List<StopEntity> stops = new LinkedList<>();
        stopTimes.forEach(it -> {
            StopEntity stop = it.getStop();
            stop.setStopTimes(null);
            stops.add(stop);
        });
        return stops;
    }

    public List<StopEntity> getStopsByRouteId(long routeId) {
        TripEntity trip = tripService.getTripByRouteId(routeId);
        return getStopsByTripId(trip.getTripId());
    }

    public Queue<TripEntityEarliestTimeItem> sortEarliestTripsFromAStopOnRouteHeap(
            List<TripEntity> trips, long stopId, long time) {
        if (trips == null || trips.isEmpty()) return null;

        final var heap = new PriorityQueue<TripEntityEarliestTimeItem>(Comparator.comparingLong(
                TripEntityEarliestTimeItem::getEarliestTime));

        for (TripEntity trip : trips) {
            List<StopTimeEntity> stopTimes = trip.getStopTimes();
            var stopTime = stopTimes.stream().filter(it -> it.getStop().getStopId() == stopId)
                                    .findAny();
            if (stopTime.isPresent()) {
                long arrival = stopTime.get().getArrival();
                if (arrival >= time) {
                    heap.add(new TripEntityEarliestTimeItem(trip, arrival));
                }
            }
        }
        return heap;
    }

    public List<TripEntity> sortEarliestTripsFromAStopOnRoute(List<TripEntity> trips, long stopId, long time) {
        if (trips == null || trips.isEmpty()) return null;

        final var heap = sortEarliestTripsFromAStopOnRouteHeap(trips, stopId, time);

        var result = new LinkedList<TripEntity>();
        while (!heap.isEmpty()) {
            result.add(heap.poll().trip);
        }

        return result;
    }

    static class TripEntityEarliestTimeItem {

        final TripEntity trip;

        @Getter
        final
        long earliestTime;

        public TripEntityEarliestTimeItem(TripEntity trip, long earliestTime) {
            this.trip = trip;
            this.earliestTime = earliestTime;
        }
    }

    public class ClientQueries {

        public List<ResponseRoutesBetween> getRoutesBetweenTwoStops(
                long sourceStopId, long destinationStopId, long time) {
            var routes = AppService.this.getRoutesBetweenTwoStops(sourceStopId, destinationStopId);
            if (routes == null || routes.isEmpty()) return Collections.emptyList();
            var response = new LinkedList<ResponseRoutesBetween>();
            for (RouteEntity route : routes) {
                var responseItem = new ResponseRoutesBetween();
                final var tripsSortedByEarliestStopTime = sortEarliestTripsFromAStopOnRoute(route.getTrips(),
                                                                                            sourceStopId, time);
                var earliestTripId = tripsSortedByEarliestStopTime.get(0).getTripId();
                final var busTimings = new LinkedList<String>();
                for (int i = 0; i < Math.min(tripsSortedByEarliestStopTime.size(), 3); i++) {
                    final var earliestStopTime = tripsSortedByEarliestStopTime.get(i).getStopTimes().stream().filter(
                            it -> it.getStop().getStopId() == sourceStopId).findFirst();
                    earliestStopTime.ifPresent(stopTimeEntity -> busTimings.add(stopTimeEntity.getArrivalString()));
                }
                responseItem
                        .setRouteId(route.getRouteId())
                        .setLongName(route.getLongName())
                        .setTripId(earliestTripId)
                        .setTravelTime(tripService.getTripTravelTimeBetweenTwoStops(
                                earliestTripId, sourceStopId, destinationStopId))
                        .setBusTimings(busTimings);
                response.add(responseItem);
            }
            return response;
        }
    }
}
