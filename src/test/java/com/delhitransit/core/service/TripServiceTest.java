package com.delhitransit.core.service;

import com.delhitransit.core.EntityGenerator;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TripServiceTest {

    private final StopEntity sourceStop = EntityGenerator.StopEntityGenerator.generate();

    private final StopEntity destinationStop = EntityGenerator.StopEntityGenerator.generate();

    private TripService service;

    private TripEntity tripEntity;

    private StopTimeEntity sourceStopTime;

    private StopTimeEntity destinationStopTime;

    @BeforeEach
    void setup() {

        sourceStopTime = new StopTimeEntity();
        sourceStopTime.setStop(sourceStop);
        sourceStopTime.setStopSequence(1);

        destinationStopTime = new StopTimeEntity();
        destinationStopTime.setStop(destinationStop);
        destinationStopTime.setStopSequence(sourceStopTime.getStopSequence() + 1);

        tripEntity = new TripEntity();
        List<StopTimeEntity> stopTimesList = new LinkedList<>();
        stopTimesList.add(sourceStopTime);
        stopTimesList.add(destinationStopTime);
        tripEntity.setStopTimes(stopTimesList);
        sourceStopTime.setTrip(tripEntity);
        destinationStopTime.setTrip(tripEntity);

        TripRepository mockTripRepository = mock(TripRepository.class);
        when(mockTripRepository.findFirstByTripId(tripEntity.getTripId()))
                .thenReturn(tripEntity);

        service = new TripService(mockTripRepository);
    }

    @Test
    void findFirstByTripIdTest() {
        TripEntity trip = service.getTripByTripId(tripEntity.getTripId());
        assertEquals(tripEntity, trip);
    }

    @Test
    void getTripTravelTimeBetweenTwoStopsTest() {
        long travelTime = service.getTripTravelTimeBetweenTwoStops(
                tripEntity.getTripId(), sourceStop.getStopId(), destinationStop.getStopId());
        assertEquals(destinationStopTime.getArrival() - sourceStopTime.getArrival(), travelTime);
    }

}
