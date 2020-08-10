/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.StopTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StopTimeEntityTest {

    @Test
    void entityFromParseableConstructorTest() {
        String arrival = "15:15:00";
        String departure = "22:30:00";
        int stopSequence = 4612;

        StopTime stopTime = new StopTime()
                .setArrival(arrival)
                .setDeparture(departure)
                .setStopSequence(stopSequence);

        StopTimeEntity stopTimeEntity = new StopTimeEntity(stopTime);
        assertEquals(arrival, stopTimeEntity.getArrivalString());
        assertEquals(departure, stopTimeEntity.getDepartureString());
        assertEquals(stopSequence, stopTimeEntity.getStopSequence());
    }

}
