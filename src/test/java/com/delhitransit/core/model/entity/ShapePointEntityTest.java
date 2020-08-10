/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.ShapePoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ShapePointEntityTest {

    @Test
    void entityFromParseableConstructorTest() {
        int shapeId = 001;
        Double latitude = 77.1108082069294;
        Double longitude = 28.6251975188353;
        int sequence = 1234;

        ShapePoint shapePoint = new ShapePoint().setShapeId(shapeId)
                                                .setLatitude(latitude)
                                                .setLongitude(longitude)
                                                .setSequence(sequence);

        ShapePointEntity shapePointEntity = new ShapePointEntity(shapePoint);
        assertEquals(shapeId, shapePointEntity.getShapeId());
        assertEquals(latitude, shapePointEntity.getLatitude());
        assertEquals(longitude, shapePointEntity.getLongitude());
        assertEquals(sequence, shapePointEntity.getSequence());

    }

}
