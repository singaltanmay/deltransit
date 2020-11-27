package com.delhitransit.core.service;

import com.delhitransit.core.EntityGenerator;
import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeEach
    void setup() {

        StopTimeEntity sourceStopTime = new StopTimeEntity();
        sourceStopTime.setStop(sourceStop);
        sourceStopTime.setStopSequence(1);

        StopTimeEntity destinationStopTime = new StopTimeEntity();
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

        service = new AppService(null, null, null, mockStopTimeService, mockTripService);

    }

    @Test
    void getRoutesBetweenTwoStopsTest() {
        List<RouteEntity> routes = service.getRoutesBetweenTwoStops(
                sourceStop.getStopId(), destinationStop.getStopId());
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllShapePointsByTripIdTest() {
        List<ShapePointEntity> entities = service.getShapePointsByTripId(tripEntity.getTripId());
        assertEntityIdenticalToShapePointEntity(entities);
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
