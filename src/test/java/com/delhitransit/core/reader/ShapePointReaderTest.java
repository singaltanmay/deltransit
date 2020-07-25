/*
 * @author Ankit Varshney
 */


package com.delhitransit.core.reader;

import com.delhitransit.core.model.parseable.ShapePoint;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShapePointReaderTest {

    private final int SHAPE_ID = 692;

    private final Double LATITUDE = 28.625705;

    private final Double LOGITUDE = 77.110839;

    private final int SEQUENCE = 712;

    ShapePointReader reader = new ShapePointReader();

    @Test
    void readLine() {
        String line = SHAPE_ID + "," + LATITUDE + "," + LOGITUDE + "," + SEQUENCE;
        ShapePoint shapePoint = reader.readLine(line);
        assertNotNull(shapePoint);
        assertEquals(SHAPE_ID, shapePoint.getShapeId());
        assertEquals(LATITUDE, shapePoint.getLatitude());
        assertEquals(LOGITUDE, shapePoint.getLongitude());
        assertEquals(SEQUENCE, shapePoint.getSequence());

    }

    @Test
    void read() throws IOException {
        List<ShapePoint> shapePoints = reader.read("src/test/resources/static/shape_point_test.txt");

        assertNotNull(shapePoints);
        assertEquals(1, shapePoints.size());

        for (ShapePoint shapePoint : shapePoints) {
            assertNotNull(shapePoint);
            assertEquals(692, shapePoint.getShapeId());
            assertEquals(28.625705, shapePoint.getLatitude());
            assertEquals(77.110839, shapePoint.getLongitude());
            assertEquals(712, shapePoint.getSequence());
        }
    }
}
