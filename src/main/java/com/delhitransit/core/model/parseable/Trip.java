/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.parseable;

/**
 * For more information see https://developers.google.com/transit/gtfs/reference/#tripstxt
 */
public class Trip {

    /**
     * Identifies the route that this trip is a part of.
     */
    private int routeId;

    /**
     * Identifies a trip.
     */
    private String tripId;

    /**
     * Identifies a geospatial shape that describes the vehicle travel path for a trip.
     */
    private int shapeId;

    public int getRouteId() {
        return routeId;
    }

    public Trip setRouteId(int routeId) {
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

    public int getShapeId() {
        return shapeId;
    }

    public Trip setShapeId(int shapeId) {
        this.shapeId = shapeId;
        return this;
    }

}
