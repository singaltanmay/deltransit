/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.reader;

import com.delhitransit.core.model.parseable.Trip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class TripReader {

    public List<Trip> read() throws IOException {
        return read("src/main/resources/static/trips.txt");
    }

    public List<Trip> read(String filepath) throws IOException {
        List<Trip> trips = new LinkedList<>();

        FileInputStream fileInputStream = new FileInputStream(new File(filepath));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((fileInputStream)));

        String line = bufferedReader.readLine();

        while (line != null) {

            line = bufferedReader.readLine();
            Trip trip = readLine(line);
            if (trip != null) {
                trips.add(trip);
            }
        }
        bufferedReader.close();

        return trips;

    }

    public Trip readLine(String line) {

        if (line != null && !line.isBlank()) {
            String[] strings = line.split(",");
            switch (strings.length) {
                case 3:
                    return new Trip()
                            .setRouteId(Integer.parseInt(strings[0]))
                            .setTripId(strings[2]);
                case 4:
                    return new Trip()
                            .setRouteId(Integer.parseInt(strings[0]))
                            .setTripId(strings[2])
                            .setShapeId(Integer.parseInt(strings[3]));
                default:
                    System.err.println(
                            "Skipped reading line due to missing data." +
                                    " Expected length was either 4 or 3 but instead found " + strings.length + "." +
                                    " String: " + line);
                    break;
            }
        }
        return null;
    }
}
