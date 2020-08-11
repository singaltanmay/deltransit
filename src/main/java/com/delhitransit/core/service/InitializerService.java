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
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

@Service
public class InitializerService {

    private List<RouteEntity> allRoutes;

    private List<StopEntity> allStops;

    private List<TripEntity> allTrips;

    private List<ShapePointEntity> allShapePoints;

    private List<StopTimeEntity> allStopTimes;

    public void init() throws IOException {
        AtomicReference<Short> threadCount = new AtomicReference<>((short) 0);
        new Thread(() -> {
            try {
                initRoutesEntityList();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                threadCount.getAndSet((short) (threadCount.get() - 1));
            }
        }).start();
        threadCount.getAndSet((short) (threadCount.get() + 1));
        new Thread(() -> {
            try {
                initShapePointsEntityList();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                threadCount.getAndSet((short) (threadCount.get() - 1));
            }
        }).start();
        threadCount.getAndSet((short) (threadCount.get() + 1));

        // Wait for threads to finish executing
        while (threadCount.get() != 0) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        initTripsEntityList();
        initStopsEntityList();
        initStopTimesEntityList();
    }

    private List<RouteEntity> initRoutesEntityList() throws IOException {
        List<RouteEntity> routeEntities = new LinkedList<>();
        List<Route> routes = new RouteReader().read();
        for (Route route : routes) {
            RouteEntity entity = new RouteEntity(route);
            entity.setTrips(new LinkedList<>());
            routeEntities.add(entity);
        }
        allRoutes = routeEntities;
        return routeEntities;
    }

    private List<ShapePointEntity> initShapePointsEntityList() throws IOException {
        List<ShapePointEntity> shapePointEntities = new ArrayList<>();
        List<ShapePoint> shapePoints = new ShapePointReader().read();
        for (ShapePoint shapePoint : shapePoints) {
            ShapePointEntity entity = new ShapePointEntity(shapePoint);
            entity.setTrips(new LinkedList<>());
            shapePointEntities.add(entity);
        }
        allShapePoints = shapePointEntities;
        return shapePointEntities;
    }

    private List<TripEntity> initTripsEntityList() throws IOException {
        List<TripEntity> tripEntities = new ArrayList<>();
        List<Trip> trips = new TripReader().read();
        for (Trip trip : trips) {
            TripEntity entity = new TripEntity(trip);
            entity.setStopTimes(new LinkedList<>());
            tripEntities.add(entity);
        }
        allTrips = tripEntities;
        return tripEntities;
    }

    private List<StopEntity> initStopsEntityList() throws IOException {
        List<StopEntity> stopEntities = new ArrayList<>();
        List<Stop> stops = new StopReader().read();
        for (Stop stop : stops) {
            StopEntity entity = new StopEntity(stop);
            entity.setStopTimes(new LinkedList<>());
            stopEntities.add(entity);
        }
        allStops = stopEntities;
        return stopEntities;
    }

    private List<StopTimeEntity> initStopTimesEntityList() throws IOException {
        List<StopTimeEntity> stopTimeEntities = new ArrayList<>();
        List<StopTime> stopTimes = new StopTimeReader().read();
        for (StopTime stopTime : stopTimes) {

            StopTimeEntity entity = new StopTimeEntity(stopTime);
            stopTimeEntities.add(entity);

            new Thread(() -> {
                allStops.parallelStream().filter(stopEntity -> stopEntity.getStopId() == stopTime.getStopId())
                        .findFirst().ifPresent(filteredStopEntity -> {
                    filteredStopEntity.getStopTimes().add(entity);
                    entity.setStop(filteredStopEntity);
                });
            }).start();

            new Thread(() -> {
                allTrips.parallelStream().filter(tripEntity -> tripEntity.getTripId().equals(stopTime.getTripId()))
                        .findFirst().ifPresent(filteredTripEntity -> {
                    filteredTripEntity.getStopTimes().add(entity);
                    entity.setTrip(filteredTripEntity);
                });
            }).start();
            System.out.println("Added stopTime: " + stopTime.getStopId());

        }
        allStopTimes = stopTimeEntities;
        return stopTimeEntities;
    }

}
