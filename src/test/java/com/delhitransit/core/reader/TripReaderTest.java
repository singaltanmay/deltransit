/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.reader;

import com.delhitransit.core.model.Trip;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TripReaderTest {

    TripReader reader = new TripReader();

    @Test
    void readLine(){
        Trip trip = reader.readLine("1,12,1_11_10,13");
        assertNotNull(trip);
        assertEquals("1", trip.getRouteId());
        assertEquals("1_11_10", trip.getTripId());
        assertEquals("13", trip.getShapeId());

    }

    @Test
    void read() throws IOException {
        List<Trip> trips = reader.read("src/test/resources/static/trips_test.txt");
        assertNotNull(trips);
        assertEquals(3, trips.size());

        for (Trip trip : trips) {
            assertNotNull(trip);
            assertEquals("1", trip.getRouteId());
            String tripId = trip.getTripId();
            assertTrue(tripId.equals("1_11_10") || tripId.equals("1_11_103"));
            Long shapeId = trip.getShapeId();
            assertTrue(shapeId.equals("") || shapeId.equals(130));


        }


    }
}
