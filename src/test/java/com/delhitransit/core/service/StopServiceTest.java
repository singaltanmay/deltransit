/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.EntityGenerator.StopEntityGenerator;
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

    private StopEntity stopEntity = StopEntityGenerator.generateStops();

    private StopService stopService;

    @BeforeEach
    void setup(){
        StopRepository mockStopRepository = mock(StopRepository.class);

        Mockito.when(mockStopRepository.findAllByNameContains(stopEntity.getName()))
               .thenReturn(Collections.singletonList(stopEntity));

        stopService = new StopService(mockStopRepository);
    }

    @Test
    void findAllByStopNameStartingWith() {
        List<StopEntity> stopEntities = stopService.getStopsByNameContains(stopEntity.getName());
        assertEntityIdenticalToStopEntity(stopEntities);
    }

    void assertEntityIdenticalToStopEntity(List<StopEntity> stopEntities) {
        assertNotNull(stopEntities);
        assertEquals(1, stopEntities.size());
        assertEquals(stopEntity, stopEntities.get(0));
    }

}
