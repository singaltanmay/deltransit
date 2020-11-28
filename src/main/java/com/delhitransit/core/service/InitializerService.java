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
import com.delhitransit.core.repository.StopTimeRepository;
import com.delhitransit.core.repository.TripRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@Service
public class InitializerService {

    private final OtdParserConnector otdParserConnector;

    private final StopTimeRepository stopTimeRepository;

    private final TripRepository tripRepository;

    private final HashMap<String, List<TripEntity>> tripsEntitiesHashMap = new HashMap<>();

    private final HashMap<Long, List<RouteEntity>> routesEntitiesHashMap = new HashMap<>();

    private final HashMap<Long, List<StopEntity>> stopsEntitiesHashMap = new HashMap<>();

    private final HashMap<Integer, List<ShapePointEntity>> shapePointsEntitiesHashMap = new HashMap<>();

    private List<StopTimeEntity> allStopTimes;

    @Autowired
    public InitializerService(RestTemplateBuilder restTemplateBuilder,
                              StopTimeRepository stopTimeRepository, TripRepository tripRepository) {
        this.otdParserConnector = new OtdParserConnector(restTemplateBuilder.build());
        this.stopTimeRepository = stopTimeRepository;
        this.tripRepository = tripRepository;
    }

    public void init(Optional<String> otdUrl) {
        if (otdUrl != null && otdUrl.isPresent()){
            String s = otdUrl.get();
            if (!s.isBlank()){
                otdParserConnector.setServerBaseUrl(s);
            }
        }
        final AtomicReference<Short> initThreadCount = new AtomicReference<>((short) 0);
        new Thread(() -> {
            initRoutesEntityList();
            initThreadCount.getAndSet((short) (initThreadCount.get() - 1));
        }).start();
        initThreadCount.getAndSet((short) (initThreadCount.get() + 1));
        new Thread(() -> {
            initShapePointsEntityList();
            initThreadCount.getAndSet((short) (initThreadCount.get() - 1));
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

        HashSet<TripEntity> tripEntities = new HashSet<>();
        Collection<List<TripEntity>> values = tripsEntitiesHashMap.values();
        for (List<TripEntity> it : values) {
            tripEntities.addAll(it);
        }
        tripRepository.saveAll(tripEntities);
        //stopTimeRepository.saveAll(allStopTimes);

        System.out.println(this.getClass().getSimpleName() + ": Database has been initialized successfully.");
    }

    private void initRoutesEntityList() {
        List<Route> routes = otdParserConnector.getAllRoutes();
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
        System.out.println("All routes initialized");
    }

    private void initShapePointsEntityList() {
        List<ShapePoint> shapePoints = otdParserConnector.getAllShapePoints();
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
        System.out.println("All shape points initialized");
    }

    private void initTripsEntityList() {
        List<Trip> rawTrips = otdParserConnector.getAllTrips();
        for (Trip trip : rawTrips) {
            TripEntity entity = new TripEntity(trip);
            entity.setStopTimes(new LinkedList<>());

            List<ShapePointEntity> shapePointEntities = shapePointsEntitiesHashMap.get(trip.getShapeId());
            if(shapePointEntities!=null && !shapePointEntities.isEmpty()) {
                for (ShapePointEntity shapePointEntity : shapePointEntities) {
                    shapePointEntity.getTrips().add(entity);
                    List<ShapePointEntity> shapePoints = entity.getShapePoints();
                    if (shapePoints == null) {
                        shapePoints = new LinkedList<>();
                        entity.setShapePoints(shapePoints);
                    }
                    shapePoints.add(shapePointEntity);
                }
            }

            List<RouteEntity> routeEntities = routesEntitiesHashMap.get((long) trip.getRouteId());
            if (routeEntities != null && !routeEntities.isEmpty()) {
                routeEntities.forEach(it -> {
                    it.getTrips().add(entity);
                    entity.setRoute(it);
                });
            }

            String tripId = entity.getTripId();
            if (tripsEntitiesHashMap.containsKey(tripId)) {
                tripsEntitiesHashMap.get(tripId).add(entity);
            } else {
                LinkedList<TripEntity> list = new LinkedList<>();
                list.add(entity);
                tripsEntitiesHashMap.put(tripId, list);
            }
        }
        System.out.println("All trips initialized");
    }

    private void initStopsEntityList() {
        List<Stop> stops = otdParserConnector.getAllStops();
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
        System.out.println("All stops initialized");
    }

    private List<StopTimeEntity> initStopTimesEntityList() {
        List<StopTimeEntity> stopTimeEntities = new ArrayList<>();
        List<StopTime> stopTimes = otdParserConnector.getAllStopTimes();

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
        System.out.println("All stop times initialized");
        return stopTimeEntities;
    }

    private static class OtdParserConnector {

        private final RestTemplate restTemplate;

        @Setter
        private String serverBaseUrl = "https://otd-parser.herokuapp.com/v1/";

        private OtdParserConnector(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        /**
         * Calling this method before making an OTD=parser API call will try to ensure initialization of OTD. This
         * method calls the 'status' endpoint exposed by OTD-parser's Admin API to get it's init status. If upstream
         * hasn't been initialized yet, the method backs-off for a random amount of time (between 1 to 5 seconds) to
         * give some time for resource to become available. The algorithm stops trying after a set number of
         * unsuccessful attempts.
         */
        private void backOffTillUpstreamInitialized() {
            String url = serverBaseUrl + "admin/init/status";
            boolean isInitialized = isOtdParserAvailable(url);
            short attempts = 5;
            while (attempts-- > 0 && !isInitialized) {
                try {
                    Thread.sleep((new Random().nextInt(4) + 1) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isInitialized = isOtdParserAvailable(url);
                System.err.println("OTD parser connection attempts remaining: " + attempts);
            }
        }

        private boolean isOtdParserAvailable(String url) {
            Boolean isInitialized = false;
            try {
                isInitialized = this.restTemplate.getForObject(url, Boolean.class);
            } catch (HttpServerErrorException e) {
                System.err.println("OTD parser is not available.");
            }
            return isInitialized != null && isInitialized;
        }

        public List<Route> getAllRoutes() {
            backOffTillUpstreamInitialized();
            String url = serverBaseUrl + "routes";
            Route[] routes = this.restTemplate.getForObject(url, Route[].class);
            return Arrays.asList(routes != null ? routes : new Route[0]);
        }

        public List<Trip> getAllTrips() {
            backOffTillUpstreamInitialized();
            String url = serverBaseUrl + "trips";
            Trip[] trips = this.restTemplate.getForObject(url, Trip[].class);
            return Arrays.asList(trips != null ? trips : new Trip[0]);
        }

        public List<Stop> getAllStops() {
            backOffTillUpstreamInitialized();
            String url = serverBaseUrl + "stops";
            Stop[] stops = this.restTemplate.getForObject(url, Stop[].class);
            return Arrays.asList(stops != null ? stops : new Stop[0]);
        }

        public List<StopTime> getAllStopTimes() {
            backOffTillUpstreamInitialized();
            String url = serverBaseUrl + "stopTimes";
            StopTime[] stopTimes = this.restTemplate.getForObject(url, StopTime[].class);
            return Arrays.asList(stopTimes != null ? stopTimes : new StopTime[0]);
        }

        public List<ShapePoint> getAllShapePoints() {
            backOffTillUpstreamInitialized();
            String url = serverBaseUrl + "shapePoints";
            ShapePoint[] shapePoints = this.restTemplate.getForObject(url, ShapePoint[].class);
            return Arrays.asList(shapePoints != null ? shapePoints : new ShapePoint[0]);
        }


    }

}
