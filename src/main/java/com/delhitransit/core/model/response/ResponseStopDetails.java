package com.delhitransit.core.model.response;

import lombok.Getter;

public class ResponseStopDetails implements Comparable<ResponseStopDetails>{

    @Getter
    private String routeLongName;

    @Getter
    private long routeId;

    @Getter
    private String tripId;

    @Getter
    private String lastStopName;

    @Getter
    private long earliestTime;

    public ResponseStopDetails setRouteLongName(String routeLongName) {
        this.routeLongName = routeLongName;
        return this;
    }

    public ResponseStopDetails setRouteId(long routeId) {
        this.routeId = routeId;
        return this;
    }

    public ResponseStopDetails setTripId(String tripId) {
        this.tripId = tripId;
        return this;
    }

    public ResponseStopDetails setLastStopName(String lastStopName) {
        this.lastStopName = lastStopName;
        return this;
    }

    public ResponseStopDetails setEarliestTime(long earliestTime) {
        this.earliestTime = earliestTime;
        return this;
    }

    @Override
    public int compareTo(ResponseStopDetails o) {
        return this.getEarliestTime() < o.getEarliestTime() ? -1 : 1;
    }
}
