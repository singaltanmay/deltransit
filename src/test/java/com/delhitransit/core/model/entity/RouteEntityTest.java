/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.Route;
import org.junit.jupiter.api.Test;

import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.AERIAL_LIFT;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.BUS;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.CABLE_TRAM;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.FERRY;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.FUNICULAR;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.MONORAIL;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.RAIL;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.STREET_LEVEL_RAIL;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.SUBWAY;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.TROLLEYBUS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteEntityTest {

    @Test
    void getRouteTypeTest() {
        assertEquals(STREET_LEVEL_RAIL, RouteEntity.getRouteType(0));
        assertEquals(SUBWAY, RouteEntity.getRouteType(1));
        assertEquals(RAIL, RouteEntity.getRouteType(2));
        assertEquals(BUS, RouteEntity.getRouteType(3));
        assertEquals(FERRY, RouteEntity.getRouteType(4));
        assertEquals(CABLE_TRAM, RouteEntity.getRouteType(5));
        assertEquals(AERIAL_LIFT, RouteEntity.getRouteType(6));
        assertEquals(FUNICULAR, RouteEntity.getRouteType(7));
        assertEquals(TROLLEYBUS, RouteEntity.getRouteType(8));
        assertEquals(MONORAIL, RouteEntity.getRouteType(9));
    }

    @Test
    void entityFromParseableConstructorTest() {
        long routeId = 1;
        String shortName = "shortName";
        String longName = "longName";

        Route route = new Route()
                .setRouteId(routeId)
                .setShortName(shortName)
                .setLongName(longName)
                .setType(5);
        RouteEntity entity = new RouteEntity(route);
        assertEquals(routeId, entity.getRouteId());
        assertEquals(shortName, entity.getShortName());
        assertEquals(longName, entity.getLongName());
        assertEquals(CABLE_TRAM, entity.getType());
    }

}
