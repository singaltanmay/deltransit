package com.delhitransit.core.model.response;

import lombok.Getter;
import lombok.Setter;

public class ResponseStopsByTrip {

    @Getter
    @Setter
    private long stopId;

    @Getter
    @Setter
    private String stopName;

    @Getter
    @Setter
    private double latitude;

    @Getter
    @Setter
    private double longitude;


    @Getter
    @Setter
    private String tripId;

    @Getter
    @Setter
    private long arrivalTime;

    public ResponseStopsByTrip(long stopId, String stopName, double latitude, double longitude, String tripId,
                               long arrivalTime) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tripId = tripId;
        this.arrivalTime = arrivalTime;
    }
}
