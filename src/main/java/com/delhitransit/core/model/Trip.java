/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model;

/**
 * For more information see https://developers.google.com/transit/gtfs/reference/#tripstxt
 */
public class Trip {

    /**
     * Identifies the route that this trip is a part of.
     */
    private long routeId;

    /**
     * Identifies a trip.
     */
    private String tripId;

    /**
     * Identifies a geospatial shape that describes the vehicle travel path for a trip.
     */
    private long shapeId;

    public long getRouteId() {
        return routeId;
    }

    public Trip setRouteId(long routeId) {
        this.routeId = routeId;
        return this;
    }

    public String getTripId() {
        return tripId;
    }

    public Trip setTripId(String tripId) {
        this.tripId = tripId;
        return this;
    }

    public long getShapeId() {
        return shapeId;
    }

    public Trip setShapeId(long shapeId) {
        this.shapeId = shapeId;
        return this;
    }

}
