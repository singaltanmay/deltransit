package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.model.response.ResponseRoutesBetween;
import com.delhitransit.core.model.response.ResponseStopDetails;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    public List<ResponseRoutesBetween> getRoutesBetweenTwoStopsCustomResponse(
            long sourceStopId, long destinationStopId, long time) {
        List<StopTimeEntity> sourceStopTimes = stopTimeService.getAllStopTimesByStopId(sourceStopId);
        List<StopTimeEntity> destinationStopTimes = stopTimeService.getAllStopTimesByStopId(destinationStopId);

        HashMap<String, Long> sourceTrips = new HashMap<>();
        for (StopTimeEntity stopTime : sourceStopTimes) {
            sourceTrips.putIfAbsent(stopTime.getTrip().getTripId(), stopTime.getArrival());
        }

        HashSet<TripEntityEarliestTimeItem> candidateTrips = new HashSet<>();
        StopTimeService.sortStopTimesByStopArrivalTime(destinationStopTimes);
        for (StopTimeEntity stopTime : destinationStopTimes) {
            TripEntity trip = stopTime.getTrip();
            if (stopTime.getArrival() >= time && sourceTrips.containsKey(trip.getTripId()) &&
                    sourceTrips.get(trip.getTripId()) <= stopTime.getArrival()) {
                candidateTrips.add(
                        new TripEntityEarliestTimeItem(trip, stopTime.getArrival(), stopTime.getArrivalString()));
            }
        }

        HashSet<RouteEntity> routes = new HashSet<>();
        candidateTrips.forEach(it -> routes.add(it.getTrip().getRoute()));

        var result = new LinkedList<ResponseRoutesBetween>();

        for (RouteEntity route : routes) {
            var response = new ResponseRoutesBetween()
                    .setLongName(route.getLongName())
                    .setRouteId(route.getRouteId());

            List<TripEntityEarliestTimeItem> routeTrips = new LinkedList<>();
            var candidateTripsArrayList = new ArrayList<>(candidateTrips);
            candidateTripsArrayList.forEach(it -> {
                if (it.getTrip().getRoute().getRouteId() == route.getRouteId()) {
                    routeTrips.add(it);
                }
            });

            var travelTime = tripService.getTripTravelTimeBetweenTwoStops(
                    routeTrips.get(0).getTrip().getTripId(), sourceStopId, destinationStopId);
            response.setTravelTime(travelTime);

            Collections.sort(routeTrips);
            final var busTimings = new LinkedList<TripEntityEarliestTimeItem>();
            for (int i = 0; i < routeTrips.size(); i++) {
                final var item = routeTrips.get(i);
                if (i == 0) response.setTripId(item.getTrip().getTripId());
                busTimings.add(item);
            }
            List<String> busTimingsStrings = new LinkedList<>();
            for (int i = 0; i < Math.min(busTimings.size(), 3); i++) {
                busTimingsStrings.add(busTimings.get(i).getEarliestTimeString());
            }
            response.setBusTimings(busTimingsStrings);

            result.add(response);
        }

        return result;
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

    public List<ResponseStopDetails> getRoutesByStopArrivalTimeCustomResponse(long stopId, long arrivalTime) {
        var stopTimes = stopTimeService.getAllStopTimesByStopIdAndArrivalTimeAfter(stopId, arrivalTime);
        var responseSet = new HashSet<ResponseStopDetails>();

        for (StopTimeEntity stopTime : stopTimes) {
            var trip = stopTime.getTrip();
            var route = trip.getRoute();
            var exist = responseSet.stream().filter(it -> it.getRouteId() == route.getRouteId()).findAny();
            if (exist.isPresent()) {
                var item = exist.get();
                if (stopTime.getArrival() < item.getEarliestTime()) {
                    item.setTripId(trip.getTripId())
                        .setEarliestTime(stopTime.getArrival());
                }
            } else {
                var stops = StopTimeService.sortStopTimesByStopArrivalTime(trip.getStopTimes());
                StopTimeEntity lastStop = stops.size() > 0 ? stops.get(stops.size() - 1) : null;
                var details = new ResponseStopDetails()
                        .setRouteId(route.getRouteId())
                        .setRouteLongName(route.getLongName())
                        .setEarliestTime(stopTime.getArrival())
                        .setTripId(trip.getTripId())
                        .setLastStopName(lastStop != null ? lastStop.getStop().getName() : Strings.EMPTY);
                responseSet.add(details);
            }
        }
        ArrayList<ResponseStopDetails> result = new ArrayList<>(responseSet);
        Collections.sort(result);
        return result;
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

    private Queue<TripEntityEarliestTimeItem> sortEarliestTripsFromAStopOnRouteHeap(
            List<TripEntity> trips, long stopId, long time) {
        if (trips == null || trips.isEmpty()) return null;

        final var heap = new PriorityQueue<>(Comparator.comparingLong(
                TripEntityEarliestTimeItem::getEarliestTime));

        trips.parallelStream().forEach(trip -> {
            var stopTimes = trip.getStopTimes();
            var stopTime = stopTimes.parallelStream()
                                    .filter(it -> it.getStop().getStopId() == stopId).findAny();
            if (stopTime.isPresent()) {
                final var entity = stopTime.get();
                var arrival = entity.getArrival();
                if (arrival >= time) {
                    heap.add(new TripEntityEarliestTimeItem(trip, arrival, entity.getArrivalString()));
                }
            }
        });
        return heap;
    }

    private static class TripEntityEarliestTimeItem implements Comparable<TripEntityEarliestTimeItem> {

        @Getter
        private final TripEntity trip;

        @Getter
        private final long earliestTime;

        @Getter
        private final String earliestTimeString;

        public TripEntityEarliestTimeItem(TripEntity trip, long earliestTime, String earliestTimeString) {
            this.trip = trip;
            this.earliestTime = earliestTime;
            this.earliestTimeString = earliestTimeString;
        }

        @Override
        public int compareTo(TripEntityEarliestTimeItem o) {
            return this.getEarliestTime() < o.getEarliestTime() ? -1 : 1;
        }
    }

}
