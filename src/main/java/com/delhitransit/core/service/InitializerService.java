/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.model.parseable.Route;
import com.delhitransit.core.model.parseable.ShapePoint;
import com.delhitransit.core.model.parseable.Stop;
import com.delhitransit.core.model.parseable.StopTime;
import com.delhitransit.core.model.parseable.Trip;
import com.delhitransit.core.reader.RouteReader;
import com.delhitransit.core.reader.ShapePointReader;
import com.delhitransit.core.reader.StopReader;
import com.delhitransit.core.reader.StopTimeReader;
import com.delhitransit.core.reader.TripReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class InitializerService {

    private List<RouteEntity> allRoutes;

    private List<StopEntity> allStops;

    private List<TripEntity> allTrips;

    private List<ShapePointEntity> allShapePoints;

    private List<StopTimeEntity> allStopTimes;

    public void init() throws IOException {
        initRoutesEntityList();
        initShapePointsEntityList();
        initTripsEntityList();
        initStopsEntityList();
        initStopTimesEntityList();
    }

    private List<RouteEntity> initRoutesEntityList() throws IOException {
        List<RouteEntity> routeEntities = new LinkedList<>();
        List<Route> routes = new RouteReader().read();
        for (Route route : routes) {
            routeEntities.add(new RouteEntity(route));
        }
        allRoutes = routeEntities;
        return routeEntities;
    }

    private List<ShapePointEntity> initShapePointsEntityList() throws IOException {
        List<ShapePointEntity> shapePointEntities = new ArrayList<>();
        List<ShapePoint> shapePoints = new ShapePointReader().read();
        for (ShapePoint shapePoint : shapePoints) {
            shapePointEntities.add(new ShapePointEntity(shapePoint));
        }
        allShapePoints = shapePointEntities;
        return shapePointEntities;
    }

    private List<TripEntity> initTripsEntityList() throws IOException {
        List<TripEntity> tripEntities = new ArrayList<>();
        List<Trip> trips = new TripReader().read();
        for (Trip trip : trips) {
            tripEntities.add(new TripEntity(trip));
        }
        allTrips = tripEntities;
        return tripEntities;
    }

    private List<StopEntity> initStopsEntityList() throws IOException {
        List<StopEntity> stopEntities = new ArrayList<>();
        List<Stop> stops = new StopReader().read();
        for (Stop stop : stops) {
            stopEntities.add(new StopEntity(stop));
        }
        allStops = stopEntities;
        return stopEntities;
    }

    private List<StopTimeEntity> initStopTimesEntityList() throws IOException {
        List<StopTimeEntity> stopTimeEntities = new ArrayList<>();
        List<StopTime> stopTimes = new StopTimeReader().read();
        for (StopTime stopTime : stopTimes) {

            StopTimeEntity stopTimeEntity = new StopTimeEntity(stopTime);

            stopTimeEntities.add(stopTimeEntity);

            allStops.parallelStream().filter(stopEntity -> stopEntity.getStopId() == stopTime.getStopId())
                    .forEach(filteredStopEntity -> filteredStopEntity.getStopTimes().add(stopTimeEntity));

            allTrips.parallelStream().filter(tripEntity -> tripEntity.getTripId().equals(stopTime.getTripId()))
                    .forEach(filteredTripEntity -> filteredTripEntity.getStopTimes().add(stopTimeEntity));
        }
        allStopTimes = stopTimeEntities;
        return stopTimeEntities;
    }

}
