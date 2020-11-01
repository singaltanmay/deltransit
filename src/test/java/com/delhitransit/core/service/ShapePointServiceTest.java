package com.delhitransit.core.service;

import com.delhitransit.core.EntityGenerator.ShapePointEntityGenerator;
import com.delhitransit.core.EntityGenerator.TripEntityGenerator;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.repository.ShapePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShapePointServiceTest {

    private final ShapePointEntity shapePointEntity = ShapePointEntityGenerator.generate();

    private ShapePointService shapePointService;

    @BeforeEach
    void setup() {
        ShapePointRepository mockShapePointRepository = mock(ShapePointRepository.class);
        TripEntity tripEntity = TripEntityGenerator.generate();
        tripEntity.getShapePoints().add(shapePointEntity);
        shapePointEntity.getTrips().add(tripEntity);
        when(mockShapePointRepository.findAll()).thenReturn(List.of(shapePointEntity));
        shapePointService = new ShapePointService(mockShapePointRepository);
    }

    @Test
    void findAllShapePointsByTripIdTest() {
        Optional<TripEntity> someTrip = shapePointEntity.getTrips().stream().findAny();
        assertTrue(someTrip.isPresent());
        String tripId = someTrip.get().getTripId();
        List<ShapePointEntity> entities = shapePointService.getAllShapePointsByTripId(tripId);
        assertEntityIdenticalToSShapePointEntity(entities);
    }

    void assertEntityIdenticalToSShapePointEntity(List<ShapePointEntity> shapePointEntities) {
        assertNotNull(shapePointEntities);
        assertEquals(1, shapePointEntities.size());
        assertEquals(shapePointEntity, shapePointEntities.get(0));
    }

}
