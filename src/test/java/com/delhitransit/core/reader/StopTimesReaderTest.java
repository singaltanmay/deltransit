/*
 * @author nitin-singla
 */

package com.delhitransit.core.reader;


import com.delhitransit.core.model.StopTime;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StopTimesReaderTest {

    StopTimesReader reader = new StopTimesReader();

    @Test
    void readLine() {
        StopTime stopTime = reader.readLine("0_6_0,06:00:00,06:00:00,2101,0");
        assertNotNull(stopTime);
        assertEquals("0_6_0", stopTime.getTripId());
        assertTrue(LocalTime.parse("06:00:00").equals(stopTime.getArrival()));
        assertTrue(LocalTime.parse("06:00:00").equals(stopTime.getDeparture()));
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
            LocalTime arrival = stopTime.getArrival();
            assertTrue(LocalTime.parse("06:00:00").equals(arrival) || LocalTime.parse("06:04:17").equals(arrival));
            LocalTime departure = stopTime.getDeparture();
            assertTrue(LocalTime.parse("06:00:00").equals(departure) || LocalTime.parse("06:04:17")
                                                                                 .equals(departure));
            long stopId = stopTime.getStopId();
            assertTrue(2101 == stopId || 2790 == stopId);
            long stopSequence = stopTime.getStopSequence();
            assertTrue(stopSequence == 0 || stopSequence == 2);
        }
    }
}
