/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.service;

import com.delhitransit.core.EntityGenerator.RouteEntityGenerator;
import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class RouteServiceTest {

    private final RouteEntity routeEntity = RouteEntityGenerator.generate();

    private RouteService service;

    @BeforeEach
    void setup() {
        RouteRepository mockRouteRepository = mock(RouteRepository.class);

        Mockito.when(mockRouteRepository.findAllByRouteId(routeEntity.getRouteId()))
               .thenReturn(Collections.singletonList(routeEntity));

        service = new RouteService(mockRouteRepository);
    }

    @Test
    void findAllByRouteIdTest() {
        List<RouteEntity> routes = service.getRouteByRouteId(routeEntity.getRouteId());
        assertEntityListIdenticalToRouteEntity(routes);
    }

    void assertEntityListIdenticalToRouteEntity(List<RouteEntity> routes) {
        assertNotNull(routes);
        assertEquals(1, routes.size());
        assertEquals(routeEntity, routes.get(0));
    }

}
