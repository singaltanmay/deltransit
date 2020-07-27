/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.parseable;

import lombok.Getter;

/**
 * For more information see https://developers.google.com/transit/gtfs/reference/#tripstxt
 */
public class Trip {

    /**
     * Identifies the route that this trip is a part of.
     */
    @Getter
    private int routeId;

    /**
     * Identifies a trip.
     */
    @Getter
    private String tripId;

    /**
     * Identifies a geospatial shape that describes the vehicle travel path for a trip.
     */
    @Getter
    private int shapeId;

    public Trip setRouteId(int routeId) {
        this.routeId = routeId;
        return this;
    }

    public Trip setTripId(String tripId) {
        this.tripId = tripId;
        return this;
    }

    public Trip setShapeId(int shapeId) {
        this.shapeId = shapeId;
        return this;
    }

}
