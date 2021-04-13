/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.GtfsRealtime;
import com.delhitransit.core.model.entity.BusPositionEntity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RealtimeService {

    private final String LOG_TAG = RealtimeService.class.getSimpleName();

    /**
     * Initiates web update and delegates all related tasks
     */
    public List<BusPositionEntity> fetchUpdate() {

        final List<BusPositionEntity> positionList = new ArrayList<>();

        // Get the latest update from server
        List<GtfsRealtime.FeedEntity> feedEntities = fetchUpdateFromServer();

        // Parses list element into a POJO and adds it to another positionList
        for (GtfsRealtime.FeedEntity entity : feedEntities) {
            positionList.add(new BusPositionEntity().parseFrom(entity));
        }

        // Return list for displaying in GUI
        return positionList;
    }

    /**
     * Makes a HTTP request and returns the latest update in a list
     */
    private List<GtfsRealtime.FeedEntity> fetchUpdateFromServer() {

        List<GtfsRealtime.FeedEntity> feedEntityList = null;
        String urlString = "https://otd.delhi.gov.in/api/realtime/VehiclePositions" +
                ".pb?key=ObnMpq02enRlNc8m2iuOnR97GLnfKQUj";

        try {

            URL url = new URL(urlString);
            InputStream inputStream = null;

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {

                System.out.println(LOG_TAG + ": Connected to server " + urlConnection.getResponseMessage());

                inputStream = urlConnection.getInputStream();

                GtfsRealtime.FeedMessage feedMessage = GtfsRealtime.FeedMessage.parseFrom(inputStream);
                feedEntityList = feedMessage.getEntityList();

                System.out.println(
                        LOG_TAG + ": Size of FeedEntity list received from server : " + feedEntityList.size());

            } else
                System.out.println(
                        LOG_TAG + ": Error connecting to server. Response code : " + urlConnection.getResponseCode());

            if (inputStream != null) {
                inputStream.close();
            }
            urlConnection.disconnect();

        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return feedEntityList;
    }
}
