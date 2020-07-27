/*
 * @author Ankit Varshney
 */
package com.delhitransit.core.reader;

import com.delhitransit.core.model.parseable.Trip;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TripReaderTest {

    TripReader reader = new TripReader();

    @Test
    void readLine() {
        Trip trip = reader.readLine("1,12,1_11_10,13");
        assertNotNull(trip);
        assertEquals(1, trip.getRouteId());
        assertEquals("1_11_10", trip.getTripId());
        assertEquals(13, trip.getShapeId());

        Trip trip1 = reader.readLine("2,11,2_12_11,");
        assertNotNull(trip1);
        assertEquals(2, trip1.getRouteId());
        assertEquals("2_12_11", trip1.getTripId());
    }

    @Test
    void read() throws IOException {
        List<Trip> trips = reader.read("src/test/resources/static/trips_test.txt");
        assertNotNull(trips);
        assertEquals(2, trips.size());

        for (Trip trip : trips) {
            assertNotNull(trip);
            int routeId = trip.getRouteId();
            assertTrue(routeId == 1 || routeId == 2);
            String tripId = trip.getTripId();
            assertTrue(tripId.equals("1_11_10") || tripId.equals("2_12_11"));
            int shapeId = trip.getShapeId();
            assertTrue(shapeId == 13 || shapeId == 0);
        }
    }
}
