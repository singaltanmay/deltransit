package com.delhitransit.core.reader;

import com.delhitransit.core.model.Route;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class RouteReader {

    public List<Route> read() throws IOException {
        List<Route> routes = new LinkedList<>();

        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/static/routes.txt"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        // First line of the stream
        //Being skipped as it contains headers and not actual data
        String line = bufferedReader.readLine();

        // Read till end of stream
        while (line != null) {
            // readLine() automatically moves to next line after reading
            line = bufferedReader.readLine();
            //Skip any empty lines
            if (!line.isBlank()) {
                String[] strings = line.split(",");
                if (strings.length > 5) {
                    Route route = new Route().setShortName(strings[0])
                                             .setLongName(strings[1])
                                             .setType(Route.getRouteType(Integer.parseInt(strings[2])))
                                             .setId(Long.parseLong(strings[3]))
                                             .setAgencyId(strings[4]);
                    routes.add(route);
                } else {
                    System.err.println(
                            "Skipped reading line due to missing data. Expeected length was 5 but instead found " + strings.length + ". String: " + line);
                }
            }
        }
        return routes;
    }
}