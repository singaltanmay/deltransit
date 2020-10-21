/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.EntityGenerator.StopEntityGenerator;
import com.delhitransit.core.GeoLocationHelper;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.repository.StopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class StopServiceTest {

    private final StopEntity stopEntity = StopEntityGenerator.generateStops();

    private StopService stopService;

    @BeforeEach
    void setup() {
        StopRepository mockStopRepository = mock(StopRepository.class);

        List<StopEntity> listOfStopEntity = Collections.singletonList(stopEntity);

        Mockito.when(mockStopRepository.findAllByNameContainsIgnoreCase(stopEntity.getName()))
               .thenReturn(listOfStopEntity);
        Mockito.when(mockStopRepository.findAllByNameIgnoreCase(stopEntity.getName()))
               .thenReturn(listOfStopEntity);

        GeoLocationHelper location = GeoLocationHelper.fromDegrees(stopEntity.getLatitude(), stopEntity.getLongitude());
        GeoLocationHelper[] coordinates = location.boundingCoordinates(1L, null);
        Mockito.when(mockStopRepository.findAllByLatitudeBetweenAndLongitudeBetween(
                coordinates[0].getLatitudeInDegrees(),
                coordinates[1].getLatitudeInDegrees(),
                coordinates[0].getLongitudeInDegrees(),
                coordinates[1].getLongitudeInDegrees()
        )).thenReturn(listOfStopEntity);

        stopService = new StopService(mockStopRepository);
    }

    @Test
    void findAllByExactNameTest() {
        List<StopEntity> stopEntities = stopService.getStopsByNameIgnoreCase(stopEntity.getName());
        assertEntityIdenticalToStopEntity(stopEntities);
    }

    @Test
    void findAllByNameSubsequenceTest() {
        List<StopEntity> stopEntities = stopService.getStopsByNameContainsIgnoreCase(stopEntity.getName());
        assertEntityIdenticalToStopEntity(stopEntities);
    }

    @Test
    void findAllByLatitudeBetweenAndLongitudeBetweenTest() {
        List<StopEntity> stopEntities = stopService.getStopsNearLocation(
                stopEntity.getLatitude(), stopEntity.getLongitude(), null);
        assertEntityIdenticalToStopEntity(stopEntities);
    }

    void assertEntityIdenticalToStopEntity(List<StopEntity> stopEntities) {
        assertNotNull(stopEntities);
        assertEquals(1, stopEntities.size());
        assertEquals(stopEntity, stopEntities.get(0));
    }

}
