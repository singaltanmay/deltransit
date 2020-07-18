package com.delhitransit.core.model;

import lombok.Getter;
import lombok.Setter;

/**
 * For more information see https://developers.google.com/transit/gtfs/reference/#tripstxt
 */
public class Trip {

    /**
     * Identifies the route that this trip is a part of.
     */
    @Getter
    @Setter
    private Route route;

    /**
     * Identifies a trip.
     */
    @Getter
    @Setter
    private long id;

    /**
     * Identifies a geospatial shape that describes the vehicle travel path for a trip.
     */
    @Getter
    @Setter
    private Shape shape;

}
