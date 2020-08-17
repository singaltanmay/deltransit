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
import com.delhitransit.core.repository.ShapePointRepository;
import com.delhitransit.core.repository.StopRepository;
import com.delhitransit.core.repository.StopTimeRepository;
import com.delhitransit.core.repository.TripRepository;
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

    private final ShapePointRepository shapePointRepository;

    private final TripRepository tripRepository;

    private final StopTimeRepository stopTimeRepository;

    private final StopRepository stopRepository;

    private List<RouteEntity> allRoutes;

    private List<StopEntity> allStops;

    private List<TripEntity> allTrips;

    private List<ShapePointEntity> allShapePoints;

    private List<StopTimeEntity> allStopTimes;

    private HashMap<String, List<TripEntity>> tripsEntitiesHashMap;

    private HashMap<Long, List<RouteEntity>> routesEntitiesHashMap;

    private HashMap<Integer, List<ShapePointEntity>> shapePointsEntitiesHashMap;

    private List<Trip> rawTrips = new LinkedList<>();

    @Autowired
    public InitializerService(RouteRepository routeRepository,
                              ShapePointRepository shapePointRepository,
                              TripRepository tripRepository,
                              StopTimeRepository stopTimeRepository,
                              StopRepository stopRepository) {
        this.routeRepository = routeRepository;
        this.shapePointRepository = shapePointRepository;
        this.tripRepository = tripRepository;
        this.stopTimeRepository = stopTimeRepository;
        this.stopRepository = stopRepository;
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
        tripsEntitiesHashMap = createTripsEntitiesHashMap();
        initStopsEntityList();
        initStopTimesEntityList();
        linkTripsToShapePointsAndRoutes();

        saveEntityListsToDatabase();
    }

    private void saveEntityListsToDatabase() {
        stopTimeRepository.saveAll(allStopTimes);

        final AtomicReference<Short> saveThreadCount = new AtomicReference<>((short) 0);
        new Thread(() -> {
            try {
                stopRepository.saveAll(allStops);
            } finally {
                saveThreadCount.getAndSet((short) (saveThreadCount.get() - 1));
            }
        }).start();
        saveThreadCount.getAndSet((short) (saveThreadCount.get() + 1));
        new Thread(() -> {
            try {
                tripRepository.saveAll(allTrips);
            } finally {
                saveThreadCount.getAndSet((short) (saveThreadCount.get() - 1));
            }
        }).start();
        saveThreadCount.getAndSet((short) (saveThreadCount.get() + 1));

        // Wait for threads to finish executing
        while (saveThreadCount.get() != 0) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        routeRepository.saveAll(allRoutes);
        shapePointRepository.saveAll(allShapePoints);
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

    // TODO Why isn't this being linked to routes and shape points here only?
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

    private HashMap<Long, List<RouteEntity>> createRoutesEntitiesHashMap() {
        HashMap<Long, List<RouteEntity>> map = new HashMap<>();
        allRoutes.forEach(routeEntity -> {
            Long routeId = routeEntity.getRouteId();
            if (map.containsKey(routeId)) {
                map.get(routeId).add(routeEntity);
            } else {
                LinkedList<RouteEntity> list = new LinkedList<>();
                list.add(routeEntity);
                map.put(routeId, list);
            }
        });
        return map;
    }

    private HashMap<Integer, List<ShapePointEntity>> createShapePointsEntitiesHashMap() {
        HashMap<Integer, List<ShapePointEntity>> map = new HashMap<>();
        allShapePoints.forEach(routeEntity -> {
            int routeId = routeEntity.getShapeId();
            if (map.containsKey(routeId)) {
                map.get(routeId).add(routeEntity);
            } else {
                LinkedList<ShapePointEntity> list = new LinkedList<>();
                list.add(routeEntity);
                map.put(routeId, list);
            }
        });
        return map;
    }

    private void linkTripsToShapePointsAndRoutes() {

        routesEntitiesHashMap = createRoutesEntitiesHashMap();
        shapePointsEntitiesHashMap = createShapePointsEntitiesHashMap();

        rawTrips.parallelStream()
                .forEach(rawTrip -> {
                    List<TripEntity> tripEntities = tripsEntitiesHashMap.get(rawTrip.getTripId());
                    if (tripEntities != null && tripEntities.size() > 0) {

                        List<ShapePointEntity> shapePointEntities
                                = shapePointsEntitiesHashMap.get(rawTrip.getShapeId());

                        if (shapePointEntities != null && shapePointEntities.size() > 0) {
                            shapePointEntities
                                    .forEach(filteredShapePointEntity -> {
                                        filteredShapePointEntity.setTrips(tripEntities);
                                        tripEntities.forEach(
                                                tripEntity -> tripEntity.setShapePoint(filteredShapePointEntity));
                                    });
                        }

                        List<RouteEntity> routeEntities = routesEntitiesHashMap.get((long) rawTrip.getRouteId());

                        if (routeEntities != null && routeEntities.size() > 0) {
                            routeEntities
                                    .parallelStream()
                                    .forEach(filteredRouteEntity -> {
                                        filteredRouteEntity.setTrips(tripEntities);
                                        tripEntities.forEach(tripEntity -> tripEntity.setRoute(filteredRouteEntity));
                                    });
                        }
                    }
                });
    }

}
