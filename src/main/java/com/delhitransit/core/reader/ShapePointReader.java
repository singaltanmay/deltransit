/*
 * @author Ankit Varshney
 */


package com.delhitransit.core.reader;

import com.delhitransit.core.model.parseable.ShapePoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class ShapePointReader {

    public List<ShapePoint> read() throws IOException {
        return read("src/main/resources/static/shapes.txt");
    }

    public List<ShapePoint> read(String filepath) throws IOException {
        List<ShapePoint> shapePoints = new LinkedList<>();

        FileInputStream fileInputStream = new FileInputStream(new File(filepath));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((fileInputStream)));

        String line = bufferedReader.readLine();

        while (line != null) {

            line = bufferedReader.readLine();
            ShapePoint shapePoint = readLine(line);
            if (shapePoint != null) {
                shapePoints.add(shapePoint);
            }
        }
        bufferedReader.close();

        return shapePoints;

    }

    public ShapePoint readLine(String line) {

        if (line != null && !line.isBlank()) {
            String[] strings = line.split(",");
            if (strings.length == 4) {
                return new ShapePoint()
                        .setShapeId(Integer.parseInt(strings[0]))
                        .setLatitude(Double.parseDouble(strings[1]))
                        .setLongitude(Double.parseDouble(strings[2]))
                        .setSequence(Integer.parseInt(strings[3]));
            } else {
                System.err.println(
                        "Skipped reading line due to missing data." +
                                " Expected length was 4 but instead found " + strings.length + "." +
                                " String: " + line);
            }
        }
        return null;
    }
}
