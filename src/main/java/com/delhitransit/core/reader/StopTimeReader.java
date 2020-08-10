/*
 * @author nitin-singla
 */

package com.delhitransit.core.reader;

import com.delhitransit.core.model.parseable.StopTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class StopTimeReader {

    public List<StopTime> read() throws IOException {
        return read("src/main/resources/static/stop_times.txt");
    }

    public List<StopTime> read(String filepath) throws IOException {
        List<StopTime> stopTimes = new LinkedList<>();

        FileInputStream fileInputStream = new FileInputStream(new File(filepath));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        // First line of the stream
        //Being skipped as it contains headers and not actual data
        String line = bufferedReader.readLine();

        // Read till end of stream
        while (line != null) {
            // readLine() automatically moves to next line after reading
            line = bufferedReader.readLine();
            StopTime stopTime = readLine(line);
            if (stopTime != null) {
                stopTimes.add(stopTime);
            }
        }

        bufferedReader.close();

        return stopTimes;
    }

    public StopTime readLine(String line) {
        //Skip any empty lines
        if (line != null && !line.isBlank()) {
            String[] strings = line.split(",");
            if (strings.length == 5) {
                return new StopTime()
                        .setTripId(strings[0])
                        .setArrival(strings[1])
                        .setDeparture(strings[2])
                        .setStopId(Long.parseLong(strings[3]))
                        .setStopSequence(Integer.parseInt(strings[4]));
            } else {
                System.err.println(
                        "Skipped reading line due to missing data." +
                                " Expected length was 5 but instead found " + strings.length + "." +
                                " String: " + line);
            }
        }
        return null;
    }
}
