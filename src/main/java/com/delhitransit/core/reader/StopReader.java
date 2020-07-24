/*
 * @author nitin-singla
 */

package com.delhitransit.core.reader;

import com.delhitransit.core.model.Stop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class StopReader {

    public List<Stop> read() throws IOException {
        return read("src/main/resources/static/stops.txt");
    }

    public List<Stop> read(String filepath) throws IOException {
        List<Stop> stops = new LinkedList<>();

        FileInputStream fileInputStream = new FileInputStream(new File(filepath));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        // First line of the stream
        //Being skipped as it contains headers and not actual data
        String line = bufferedReader.readLine();

        // Read till end of stream
        while (line != null) {
            // readLine() automatically moves to next line after reading
            line = bufferedReader.readLine();
            Stop stop = readLine(line);
            if (stop != null) {
                stops.add(stop);
            }
        }

        bufferedReader.close();

        return stops;
    }

    public Stop readLine(String line) {
        //Skip any empty lines
        if (line != null && !line.isBlank()) {
            String[] strings = line.split(",");
            if (strings.length == 4) {
                return new Stop()
                        .setStopId(Long.parseLong(strings[0]))
                        .setName(strings[1])
                        .setLatitude(Double.parseDouble(strings[2]))
                        .setLongitude(Double.parseDouble(strings[3]));
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





