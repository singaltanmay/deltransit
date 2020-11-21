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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

import static com.delhitransit.core.api.controller.DelhiTransitController.DEFAULT_PAGE_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class RouteServiceTest {

    private final RouteEntity routeEntity = RouteEntityGenerator.generate();

    private RouteService service;

    @BeforeEach
    void setup() {
        RouteRepository mockRouteRepository = mock(RouteRepository.class);

        Page<RouteEntity> listOfRouteEntity = new PageImpl<>(Collections.singletonList(routeEntity));

        Mockito.when(mockRouteRepository.findAllByRouteId(routeEntity.getRouteId(),DEFAULT_PAGE_REQUEST))
               .thenReturn(listOfRouteEntity);

        String shortName = routeEntity.getShortName();
        Mockito.when(mockRouteRepository.findAllByShortNameIgnoreCase(shortName, DEFAULT_PAGE_REQUEST))
               .thenReturn(listOfRouteEntity);
        Mockito.when(mockRouteRepository.findAllByShortNameContainsIgnoreCase(
                shortName.substring(0, shortName.length() / 2),DEFAULT_PAGE_REQUEST))
               .thenReturn(listOfRouteEntity);

        String longName = routeEntity.getLongName();
        Mockito.when(mockRouteRepository.findAllByLongNameIgnoreCase(longName,DEFAULT_PAGE_REQUEST))
               .thenReturn(listOfRouteEntity);
        Mockito.when(mockRouteRepository.findAllByLongNameContainsIgnoreCase(
                longName.substring(0, longName.length() / 2),DEFAULT_PAGE_REQUEST))
               .thenReturn(listOfRouteEntity);

        Mockito.when(mockRouteRepository.findAllByType(routeEntity.getType(), DEFAULT_PAGE_REQUEST))
               .thenReturn(listOfRouteEntity);

        service = new RouteService(mockRouteRepository);
    }

    @Test
    void findAllByRouteIdTest() {
        Page<RouteEntity> routes = service.getRoutesByRouteId(routeEntity.getRouteId(), DEFAULT_PAGE_REQUEST);
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByShortNameIgnoreCaseTest() {
        Page<RouteEntity> routes = service.getRoutesByShortNameIgnoreCase(routeEntity.getShortName(), DEFAULT_PAGE_REQUEST);
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByShortNameContainsIgnoreCaseTest() {
        String shortName = routeEntity.getShortName();
        Page<RouteEntity> routes = service.getRoutesByShortNameContainsIgnoreCase(
                shortName.substring(0, shortName.length() / 2), DEFAULT_PAGE_REQUEST);
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByLongNameIgnoreCaseTest() {
        Page<RouteEntity> routes = service.getRoutesByLongNameIgnoreCase(routeEntity.getLongName(),DEFAULT_PAGE_REQUEST);
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByLongNameContainsIgnoreCaseTest() {
        String longName = routeEntity.getLongName();
        Page<RouteEntity> routes = service.getRoutesByLongNameContainsIgnoreCase(
                longName.substring(0, longName.length() / 2), DEFAULT_PAGE_REQUEST);
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByTypeEnumTest() {
        Page<RouteEntity> routes = service.getRoutesByType(routeEntity.getType(), DEFAULT_PAGE_REQUEST);
        assertEntityListIdenticalToRouteEntity(routes);
    }

    @Test
    void findAllByTypeIntTest() {
        Page<RouteEntity> routes = service.getRoutesByType(RouteEntity.getRouteType(routeEntity.getType()), DEFAULT_PAGE_REQUEST);
        assertEntityListIdenticalToRouteEntity(routes);
    }

    void assertEntityListIdenticalToRouteEntity(Page<RouteEntity> page) {
        assertNotNull(page);
        List<RouteEntity> routes = page.toList();
        assertEquals(1, routes.size());
        assertEquals(routeEntity, routes.get(0));
    }

}
