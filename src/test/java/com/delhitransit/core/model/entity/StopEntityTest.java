/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.Stop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StopEntityTest {

    @Test
    void entityFromParseableConstructorTest() {
        long stopId = 83759837;
        String name = "Shastri Nagar";
        Double latitude = 1213.1234513;
        Double longitude = 1254.121546;

        Stop stop = new Stop().setStopId(stopId)
                              .setName(name)
                              .setLatitude(latitude)
                              .setLongitude(longitude);

        StopEntity stopEntity = new StopEntity(stop);
        assertEquals(stopId, stopEntity.getStopId());
        assertEquals(name, stopEntity.getName());
        assertEquals(latitude, stopEntity.getLatitude());
        assertEquals(longitude, stopEntity.getLongitude());

    }

}
