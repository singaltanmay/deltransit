package com.delhitransit.core.model.response;

import lombok.Getter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//  Custom response class for endpoint /v1/routes/between?destination=dStopId&source=sStopId
public class ResponseRoutesBetween {

    @Getter
    private String longName;

    @Getter
    private long routeId;

    @Getter
    private String tripId;

    @Getter
    private long travelTime;

    @Getter
    private List<String> busTimings = new LinkedList<>();

    public ResponseRoutesBetween setLongName(String longName) {
        this.longName = longName;
        return this;
    }

    public ResponseRoutesBetween setRouteId(long routeId) {
        this.routeId = routeId;
        return this;
    }

    public ResponseRoutesBetween setTripId(String tripId) {
        this.tripId = tripId;
        return this;
    }

    public ResponseRoutesBetween setTravelTime(long travelTime) {
        this.travelTime = travelTime;
        return this;
    }

    public ResponseRoutesBetween setBusTimings(List<String> busTimings) {
        this.busTimings = busTimings;
        return this;
    }
}
