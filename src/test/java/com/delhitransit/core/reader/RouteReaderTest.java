package com.delhitransit.core.reader;

import com.delhitransit.core.model.Route;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteReaderTest {

    RouteReader reader = new RouteReader();

    @Test
    void readLine() {
        Route route = reader.readLine(",108DOWN,3,0,DIMTS");
        assertNotNull(route);
        assertEquals("", route.getShortName());
        assertEquals("108DOWN", route.getLongName());
        assertEquals(Route.ROUTE_TYPE.BUS, route.getType());
        assertEquals(0, route.getId());
        assertEquals("DIMTS", route.getAgencyId());
    }

    @Test
    void read() throws IOException {
        List<Route> routes = reader.read("src/test/resources/static/routes_test.txt");
        assertNotNull(routes);
        assertEquals(2, routes.size());

        for (Route route : routes) {
            assertNotNull(route);
            assertEquals("", route.getShortName());
            assertEquals(Route.ROUTE_TYPE.BUS, route.getType());
            String longName = route.getLongName();
            assertTrue(longName.equals("108DOWN") || longName.equals("114DOWN_DTC"));
            String agencyId = route.getAgencyId();
            assertTrue(agencyId.equals("DIMTS") || agencyId.equals("DTC"));
        }

    }

}
