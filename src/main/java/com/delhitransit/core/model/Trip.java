package com.delhitransit.core.model;

import lombok.Getter;
import lombok.Setter;

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
