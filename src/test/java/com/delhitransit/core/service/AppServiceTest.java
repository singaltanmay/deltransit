package com.delhitransit.core.service;

import com.delhitransit.core.EntityGenerator;
import com.delhitransit.core.model.entity.RouteEntity;
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

public class AppServiceTest {

    private final StopEntity sourceStop = EntityGenerator.StopEntityGenerator.generate();

    private final StopEntity destinationStop = EntityGenerator.StopEntityGenerator.generate();

    private final RouteEntity routeEntity = EntityGenerator.RouteEntityGenerator.generate();

    private AppService service;

    @BeforeEach
    void setup() {

        StopTimeEntity sourceStopTime = new StopTimeEntity();
        sourceStopTime.setStop(sourceStop);
        sourceStopTime.setStopSequence(1);

        StopTimeEntity destinationStopTime = new StopTimeEntity();
        destinationStopTime.setStop(destinationStop);
        destinationStopTime.setStopSequence(sourceStopTime.getStopSequence() + 1);

        TripEntity trip = new TripEntity();
        trip.setRoute(routeEntity);
        List<StopTimeEntity> stopTimesList = new LinkedList<>();
        stopTimesList.add(sourceStopTime);
        stopTimesList.add(destinationStopTime);
        trip.setStopTimes(stopTimesList);
        sourceStopTime.setTrip(trip);
        destinationStopTime.setTrip(trip);

        StopTimeService mockStopTimeService = Mockito.mock(StopTimeService.class);

        Mockito.when(mockStopTimeService.getAllStopTimesByStopId(sourceStop.getStopId()))
               .thenReturn(Collections.singletonList(sourceStopTime));

        service = new AppService(null, null, null, mockStopTimeService, null);

    }

    @Test
    void getRoutesBetweenTwoStopsTest() {
        List<RouteEntity> routes = service.getRoutesBetweenTwoStops(
                sourceStop.getStopId(), destinationStop.getStopId());
        assertEntityListIdenticalToRouteEntity(routes);
    }

    void assertEntityListIdenticalToRouteEntity(List<RouteEntity> routes) {
        assertNotNull(routes);
        assertEquals(1, routes.size());
        assertEquals(routeEntity, routes.get(0));
    }

}
