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

        List<RouteEntity> listOfRouteEntity = Collections.singletonList(routeEntity);

        Mockito.when(mockRouteRepository.findAllByRouteId(routeEntity.getRouteId()))
               .thenReturn(listOfRouteEntity);

        String shortName = routeEntity.getShortName();
        Mockito.when(mockRouteRepository.findAllByShortNameIgnoreCase(shortName))
               .thenReturn(listOfRouteEntity);
        Mockito.when(mockRouteRepository.findAllByShortNameContainsIgnoreCase(
                shortName.substring(0, shortName.length() / 2)))
               .thenReturn(listOfRouteEntity);

        String longName = routeEntity.getLongName();
        Mockito.when(mockRouteRepository.findAllByLongNameIgnoreCase(longName))
               .thenReturn(listOfRouteEntity);
        Mockito.when(mockRouteRepository.findAllByLongNameContainsIgnoreCase(
                longName.substring(0, longName.length() / 2)))
               .thenReturn(listOfRouteEntity);

        Mockito.when(mockRouteRepository.findAllByType(routeEntity.getType()))
               .thenReturn(listOfRouteEntity);

        service = new RouteService(mockRouteRepository);
    }

    @Test
    void findAllByRouteIdTest() {
        List<RouteEntity> routes = service.getRoutesByRouteId(routeEntity.getRouteId());
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByShortNameIgnoreCaseTest() {
        List<RouteEntity> routes = service.getRoutesByShortNameIgnoreCase(routeEntity.getShortName());
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByShortNameContainsIgnoreCaseTest() {
        String shortName = routeEntity.getShortName();
        List<RouteEntity> routes = service.getRoutesByShortNameContainsIgnoreCase(
                shortName.substring(0, shortName.length() / 2));
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByLongNameIgnoreCaseTest() {
        List<RouteEntity> routes = service.getRoutesByLongNameIgnoreCase(routeEntity.getLongName());
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByLongNameContainsIgnoreCaseTest() {
        String longName = routeEntity.getLongName();
        List<RouteEntity> routes = service.getRoutesByLongNameContainsIgnoreCase(
                longName.substring(0, longName.length() / 2));
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByTypeEnumTest() {
        List<RouteEntity> routes = service.getRoutesByType(routeEntity.getType());
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByTypeIntTest() {
        List<RouteEntity> routes = service.getRoutesByType(RouteEntity.getRouteType(routeEntity.getType()));
        assertEntityListIdenticalToRouteEntity(routes);
    }

    void assertEntityListIdenticalToRouteEntity(List<RouteEntity> routes) {
        assertNotNull(routes);
        assertEquals(1, routes.size());
        assertEquals(routeEntity, routes.get(0));
    }

}
