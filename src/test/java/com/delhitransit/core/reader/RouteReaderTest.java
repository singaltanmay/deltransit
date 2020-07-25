/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.reader;

import com.delhitransit.core.model.parseable.Route;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RouteReaderTest {

    private final String ROUTE_SHORT_NAME = "";

    private final String ROUTE_LONG_NAME = "108DOWN";

    private final int ROUTE_TYPE = 3;

    private final String ROUTE_AGENCY_ID = "DIMTS";

    private final Long ROUTE_ROUTE_ID = 0L;

    RouteReader reader = new RouteReader();

    @Test
    void readLine() {
        String line =
                ROUTE_SHORT_NAME + "," + ROUTE_LONG_NAME + "," + ROUTE_TYPE + "," + ROUTE_ROUTE_ID + "," + ROUTE_AGENCY_ID;
        Route route = reader.readLine(line);
        assertNotNull(route);
        assertEquals(ROUTE_SHORT_NAME, route.getShortName());
        assertEquals(ROUTE_LONG_NAME, route.getLongName());
        assertEquals(ROUTE_TYPE, route.getType());
        assertEquals(ROUTE_ROUTE_ID, route.getRouteId());
        assertEquals(ROUTE_AGENCY_ID, route.getAgencyId());
    }

    @Test
    void read() throws IOException {
        List<Route> routes = reader.read("src/test/resources/static/routes_test.txt");
        assertNotNull(routes);
        assertEquals(1, routes.size());

        Route route = routes.get(0);
        assertNotNull(route);
        assertEquals(ROUTE_SHORT_NAME, route.getShortName());
        assertEquals(ROUTE_LONG_NAME, route.getLongName());
        assertEquals(ROUTE_TYPE, route.getType());
        assertEquals(ROUTE_ROUTE_ID, route.getRouteId());
        assertEquals(ROUTE_AGENCY_ID, route.getAgencyId());

    }

}
