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
import com.delhitransit.core.repository.StopTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

@Service
public class InitializerService {

    private final StopTimeRepository stopTimeRepository;

    private final HashMap<String, List<TripEntity>> tripsEntitiesHashMap = new HashMap<>();

    private final HashMap<Long, List<RouteEntity>> routesEntitiesHashMap = new HashMap<>();

    private final HashMap<Long, List<StopEntity>> stopsEntitiesHashMap = new HashMap<>();

    private final HashMap<Integer, List<ShapePointEntity>> shapePointsEntitiesHashMap = new HashMap<>();

    private List<StopTimeEntity> allStopTimes;

    @Autowired
    public InitializerService(StopTimeRepository stopTimeRepository) {
        this.stopTimeRepository = stopTimeRepository;
    }

    public void init() throws IOException {
        final AtomicReference<Short> initThreadCount = new AtomicReference<>((short) 0);
        new Thread(() -> {
            try {
                initRoutesEntityList();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                initThreadCount.getAndSet((short) (initThreadCount.get() - 1));
            }
        }).start();
        initThreadCount.getAndSet((short) (initThreadCount.get() + 1));
        new Thread(() -> {
            try {
                initShapePointsEntityList();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                initThreadCount.getAndSet((short) (initThreadCount.get() - 1));
            }
        }).start();
        initThreadCount.getAndSet((short) (initThreadCount.get() + 1));

        // Wait for threads to finish executing
        while (initThreadCount.get() != 0) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        initTripsEntityList();
        initStopsEntityList();
        initStopTimesEntityList();

        stopTimeRepository.saveAll(allStopTimes);
    }

    private void initRoutesEntityList() throws IOException {
        List<Route> routes = new RouteReader().read();
        for (Route route : routes) {
            RouteEntity entity = new RouteEntity(route);
            entity.setTrips(new LinkedList<>());

            long routeId = entity.getRouteId();
            if (routesEntitiesHashMap.containsKey(routeId)) {
                routesEntitiesHashMap.get(routeId).add(entity);
            } else {
                LinkedList<RouteEntity> list = new LinkedList<>();
                list.add(entity);
                routesEntitiesHashMap.put(routeId, list);
            }
        }
    }

    private void initShapePointsEntityList() throws IOException {
        List<ShapePoint> shapePoints = new ShapePointReader().read();
        for (ShapePoint shapePoint : shapePoints) {
            ShapePointEntity entity = new ShapePointEntity(shapePoint);
            entity.setTrips(new LinkedList<>());

            int shapeId = entity.getShapeId();
            if (shapePointsEntitiesHashMap.containsKey(shapeId)) {
                shapePointsEntitiesHashMap.get(shapeId).add(entity);
            } else {
                LinkedList<ShapePointEntity> list = new LinkedList<>();
                list.add(entity);
                shapePointsEntitiesHashMap.put(shapeId, list);
            }

        }
    }

    private void initTripsEntityList() throws IOException {
        List<Trip> rawTrips = new TripReader().read();
        for (Trip trip : rawTrips) {
            TripEntity entity = new TripEntity(trip);
            entity.setStopTimes(new LinkedList<>());

            List<ShapePointEntity> shapePointEntities = shapePointsEntitiesHashMap.get(trip.getShapeId());
            shapePointEntities.forEach(it -> {
                it.getTrips().add(entity);
                entity.setShapePoint(it);
            });

            List<RouteEntity> routeEntities = routesEntitiesHashMap.get((long) trip.getRouteId());
            routeEntities.forEach(it -> {
                it.getTrips().add(entity);
                entity.setRoute(it);
            });

            String tripId = entity.getTripId();
            if (tripsEntitiesHashMap.containsKey(tripId)) {
                tripsEntitiesHashMap.get(tripId).add(entity);
            } else {
                LinkedList<TripEntity> list = new LinkedList<>();
                list.add(entity);
                tripsEntitiesHashMap.put(tripId, list);
            }

        }
    }

    private void initStopsEntityList() throws IOException {
        List<Stop> stops = new StopReader().read();
        for (Stop stop : stops) {
            StopEntity entity = new StopEntity(stop);
            entity.setStopTimes(new LinkedList<>());

            Long stopId = entity.getStopId();
            if (stopsEntitiesHashMap.containsKey(stopId)) {
                stopsEntitiesHashMap.get(stopId).add(entity);
            } else {
                LinkedList<StopEntity> list = new LinkedList<>();
                list.add(entity);
                stopsEntitiesHashMap.put(stopId, list);
            }

        }
    }

    private List<StopTimeEntity> initStopTimesEntityList() throws IOException {
        List<StopTimeEntity> stopTimeEntities = new ArrayList<>();
        List<StopTime> stopTimes = new StopTimeReader().read();

        stopTimes.forEach(stopTime -> {
            StopTimeEntity entity = new StopTimeEntity(stopTime);

            List<StopEntity> stopEntityList = stopsEntitiesHashMap.get(stopTime.getStopId());
            if (stopEntityList != null) {
                stopEntityList.forEach(
                        filteredStopEntity -> {
                            filteredStopEntity.getStopTimes().add(entity);
                            entity.setStop(filteredStopEntity);
                        });
            }

            List<TripEntity> tripEntityList = tripsEntitiesHashMap.get(stopTime.getTripId());
            if (tripEntityList != null) {
                tripEntityList.forEach(
                        filteredTripEntity -> {
                            filteredTripEntity.getStopTimes().add(entity);
                            entity.setTrip(filteredTripEntity);
                        });
            }

            stopTimeEntities.add(entity);
        });

        allStopTimes = stopTimeEntities;
        return stopTimeEntities;
    }

}
