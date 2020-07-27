/*
 * @author nitin-singla
 */

package com.delhitransit.core.reader;


import com.delhitransit.core.model.parseable.StopTime;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StopTimeReaderTest {

    StopTimeReader reader = new StopTimeReader();

    @Test
    void readLine() {
        StopTime stopTime = reader.readLine("0_6_0,06:00:00,06:00:00,2101,0");
        assertNotNull(stopTime);
        assertEquals("0_6_0", stopTime.getTripId());
        assertEquals("06:00:00", stopTime.getArrival());
        assertEquals(("06:00:00"), stopTime.getDeparture());
        assertEquals(2101, stopTime.getStopId());
        assertEquals(0, stopTime.getStopSequence());
    }

    @Test
    void read() throws IOException {
        List<StopTime> stopTimes = reader.read("src/test/resources/static/stop_times_test.txt");
        assertNotNull(stopTimes);
        assertEquals(2, stopTimes.size());
        for (StopTime stopTime : stopTimes) {
            assertNotNull(stopTime);
            assertEquals("0_6_0", stopTime.getTripId());
            String arrival = stopTime.getArrival();
            assertTrue(("06:00:00").equals(arrival) || ("06:04:17").equals(arrival));
            String departure = stopTime.getDeparture();
            assertTrue(("06:00:00").equals(departure) || ("06:04:17").equals(departure));
            long stopId = stopTime.getStopId();
            assertTrue(2101 == stopId || 2790 == stopId);
            long stopSequence = stopTime.getStopSequence();
            assertTrue(stopSequence == 0 || stopSequence == 2);
        }
    }
}
