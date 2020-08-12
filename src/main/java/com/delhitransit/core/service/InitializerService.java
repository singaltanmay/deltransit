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
import com.delhitransit.core.repository.RouteRepository;
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

    private final RouteRepository routeRepository;

    private List<RouteEntity> allRoutes;

    private List<StopEntity> allStops;

    private List<TripEntity> allTrips;

    private List<ShapePointEntity> allShapePoints;

    private List<StopTimeEntity> allStopTimes;

    private HashMap<String, List<TripEntity>> tripsEntitiesHashMap;

    private List<Trip> rawTrips = new LinkedList<>();

    @Autowired
    public InitializerService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public void init() throws IOException {
        final AtomicReference<Short> threadCount = new AtomicReference<>((short) 0);
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
        tripsEntitiesHashMap = createTripsEntitiesHashMap();
        initStopsEntityList();
        initStopTimesEntityList();
        linkTripsToShapePointsAndRoutes();

        routeRepository.saveAll(allRoutes);
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
        rawTrips = new TripReader().read();
        for (Trip trip : rawTrips) {
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

    private HashMap<Long, List<StopEntity>> createStopsEntitiesHashMap() {
        HashMap<Long, List<StopEntity>> map = new HashMap<>();
        allStops.forEach(stopEntity -> {
            Long stopId = stopEntity.getStopId();
            if (map.containsKey(stopId)) {
                map.get(stopId).add(stopEntity);
            } else {
                LinkedList<StopEntity> list = new LinkedList<>();
                list.add(stopEntity);
                map.put(stopId, list);
            }
        });
        return map;
    }

    private HashMap<String, List<TripEntity>> createTripsEntitiesHashMap() {
        HashMap<String, List<TripEntity>> map = new HashMap<>();
        allTrips.forEach(tripEntity -> {
            String tripId = tripEntity.getTripId();
            if (map.containsKey(tripId)) {
                map.get(tripId).add(tripEntity);
            } else {
                LinkedList<TripEntity> list = new LinkedList<>();
                list.add(tripEntity);
                map.put(tripId, list);
            }
        });
        return map;
    }

    private List<StopTimeEntity> initStopTimesEntityList() throws IOException {
        List<StopTimeEntity> stopTimeEntities = new ArrayList<>();
        List<StopTime> stopTimes = new StopTimeReader().read();

        HashMap<Long, List<StopEntity>> stopsEntitiesHashMap = createStopsEntitiesHashMap();

        for (StopTime stopTime : stopTimes) {
            StopTimeEntity entity = new StopTimeEntity(stopTime);

            stopsEntitiesHashMap.get(stopTime.getStopId()).forEach(
                    filteredStopEntity -> {
                        filteredStopEntity.getStopTimes().add(entity);
                        entity.setStop(filteredStopEntity);
                    });

            tripsEntitiesHashMap.get(stopTime.getTripId()).forEach(
                    filteredTripEntity -> {
                        filteredTripEntity.getStopTimes().add(entity);
                        entity.setTrip(filteredTripEntity);
                    });

            stopTimeEntities.add(entity);
        }

        allStopTimes = stopTimeEntities;
        return stopTimeEntities;
    }

    private void linkTripsToShapePointsAndRoutes() {
        rawTrips.parallelStream()
                .forEach(rawTrip -> {
                    List<TripEntity> tripEntities = tripsEntitiesHashMap.get(rawTrip.getTripId());
                    if (tripEntities != null) {

                        allShapePoints.parallelStream()
                                      .filter(shapePointEntity -> shapePointEntity.getShapeId() == rawTrip.getShapeId())
                                      .forEach(filteredShapePointEntity -> {
                                          filteredShapePointEntity.setTrips(tripEntities);
                                          tripEntities.forEach(
                                                  tripEntity -> tripEntity.setShapePoint(filteredShapePointEntity));
                                      });

                        allRoutes.parallelStream()
                                 .filter(routeEntity -> routeEntity.getRouteId() == rawTrip.getRouteId())
                                 .forEach(filteredRouteEntity -> {
                                     filteredRouteEntity.setTrips(tripEntities);
                                     tripEntities.forEach(tripEntity -> tripEntity.setRoute(filteredRouteEntity));
                                 });
                    }
                });

    }

}
