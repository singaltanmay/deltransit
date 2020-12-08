package com.delhitransit.core.service;

import com.delhitransit.core.EntityGenerator;
import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.model.response.ResponseRoutesBetween;
import com.delhitransit.core.model.response.ResponseStopDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class AppServiceTest {

    private final StopEntity sourceStop = EntityGenerator.StopEntityGenerator.generate();

    private final StopEntity destinationStop = EntityGenerator.StopEntityGenerator.generate();

    private final RouteEntity routeEntity = EntityGenerator.RouteEntityGenerator.generate();

    private final ShapePointEntity shapePointEntity = EntityGenerator.ShapePointEntityGenerator.generate();

    private TripEntity tripEntity;

    private AppService service;

    private StopTimeEntity sourceStopTime;

    private StopTimeEntity destinationStopTime;

    @BeforeEach
    void setup() {

        sourceStopTime = new StopTimeEntity();
        sourceStopTime.setStop(sourceStop);
        sourceStopTime.setStopSequence(1);
        List<StopTimeEntity> sourceStopTimes = sourceStop.getStopTimes();
        if (sourceStopTimes == null) {
            sourceStopTimes = new LinkedList<>();
            sourceStop.setStopTimes(sourceStopTimes);
        }
        sourceStopTimes.add(sourceStopTime);

        destinationStopTime = new StopTimeEntity();
        destinationStopTime.setStop(destinationStop);
        destinationStopTime.setStopSequence(sourceStopTime.getStopSequence() + 1);

        tripEntity = new TripEntity();
        tripEntity.setRoute(routeEntity);
        List<StopTimeEntity> stopTimesList = new LinkedList<>();
        stopTimesList.add(sourceStopTime);
        stopTimesList.add(destinationStopTime);
        tripEntity.setStopTimes(stopTimesList);
        sourceStopTime.setTrip(tripEntity);
        destinationStopTime.setTrip(tripEntity);

        StopTimeService mockStopTimeService = Mockito.mock(StopTimeService.class);

        when(mockStopTimeService.getAllStopTimesByStopId(sourceStop.getStopId()))
                .thenReturn(Collections.singletonList(sourceStopTime));
        when(mockStopTimeService.getAllStopTimesByStopId(destinationStop.getStopId()))
                .thenReturn(Collections.singletonList(destinationStopTime));

        TripService mockTripService = Mockito.mock(TripService.class);
        List<TripEntity> trips = shapePointEntity.getTrips();
        if (trips == null) trips = new LinkedList<>();
        trips.add(tripEntity);
        tripEntity.setShapePoints(Collections.singletonList(shapePointEntity));
        when(mockTripService.getTripByTripId(tripEntity.getTripId()))
                .thenReturn(tripEntity);
        when(mockTripService.getTripByRouteId(routeEntity.getRouteId()))
                .thenReturn(tripEntity);
        when(mockTripService.getTripTravelTimeBetweenTwoStops(
                tripEntity.getTripId(), sourceStop.getStopId(), destinationStop.getStopId()))
                .thenReturn(destinationStopTime.getArrival() - sourceStopTime.getArrival());
        when(mockStopTimeService
                     .getAllStopTimesByStopIdAndArrivalTimeAfter(sourceStop.getStopId(), sourceStopTime.getArrival()))
                .thenReturn(Collections.singletonList(sourceStopTime));
        service = new AppService(null, null, null, mockStopTimeService, mockTripService);

    }

    @Test
    void getRoutesBetweenTwoStopsTest() {
        List<RouteEntity> routes = service.getRoutesBetweenTwoStops(
                sourceStop.getStopId(), destinationStop.getStopId());
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void getRoutesBetweenTwoStopsCustomResponseTest() {
        List<ResponseRoutesBetween> responseList = service.getRoutesBetweenTwoStopsCustomResponse(
                sourceStop.getStopId(), destinationStop.getStopId(), sourceStop.getStopTimes().get(0).getArrival()
        );
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        for (ResponseRoutesBetween response : responseList) {
            assertEquals(routeEntity.getLongName(), response.getLongName());
            assertEquals(routeEntity.getRouteId(), response.getRouteId());
            assertEquals(tripEntity.getTripId(), response.getTripId());
            assertEquals(destinationStopTime.getArrival() - sourceStopTime.getArrival(),
                         response.getTravelTime());
            List<String> busTimings = response.getBusTimings();
            assertNotNull(busTimings);
            assertEquals(1, busTimings.size());
            assertEquals(sourceStopTime.getArrivalString(), busTimings.get(0));
        }
    }

    @Test
    void findAllShapePointsByTripIdTest() {
        List<ShapePointEntity> entities = service.getShapePointsByTripId(tripEntity.getTripId());
        assertEntityIdenticalToShapePointEntity(entities);
    }

    @Test
    void getRoutesByStopIdAndStopTimeArrivalTimeTest() {
        List<RouteEntity> routes = service.getRoutesByStopIdAndStopTimeArrivalTime(
                sourceStop.getStopId(), sourceStop.getStopTimes().get(0).getArrival());
        assertNotNull(routes);
        assertFalse(routes.isEmpty());
        assertEquals(routeEntity, routes.get(0));
    }

    @Test
    void getStopsByTripIdTest() {
        List<StopEntity> stops = service.getStopsByTripId(tripEntity.getTripId());
        assertStopEntityListContainsStops(stops);
    }

    @Test
    void getStopsByRouteIdTest() {
        List<StopEntity> stops = service.getStopsByRouteId(routeEntity.getRouteId());
        assertStopEntityListContainsStops(stops);
    }

    @Test
    void getRoutesByStopArrivalTimeCustomResponseTest() {
        List<ResponseStopDetails> response = service.getRoutesByStopArrivalTimeCustomResponse(
                sourceStop.getStopId(), sourceStopTime.getArrival());
        assertNotNull(response);
        assertEquals(1,response.size());
        ResponseStopDetails details = response.get(0);
        assertEquals(routeEntity.getRouteId(),details.getRouteId());
        assertEquals(routeEntity.getLongName(),details.getRouteLongName());
        assertEquals(tripEntity.getTripId(),details.getTripId());
        assertEquals(sourceStopTime.getArrival(),details.getEarliestTime());
        assertEquals(destinationStop.getName(),details.getLastStopName());
    }

    private void assertStopEntityListContainsStops(List<StopEntity> stops) {
        assertNotNull(stops);
        assertTrue(stops.contains(sourceStop));
        assertTrue(stops.contains(destinationStop));
    }

    void assertEntityListIdenticalToRouteEntity(List<RouteEntity> routes) {
        assertNotNull(routes);
        assertEquals(1, routes.size());
        assertEquals(routeEntity, routes.get(0));
    }

    void assertEntityIdenticalToShapePointEntity(List<ShapePointEntity> shapePointEntities) {
        assertNotNull(shapePointEntities);
        assertEquals(1, shapePointEntities.size());
        assertEquals(shapePointEntity, shapePointEntities.get(0));
    }

}
