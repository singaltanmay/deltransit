package com.delhitransit.core.model;

import lombok.Getter;
import lombok.Setter;

/**
 * For more information see https://developers.google.com/transit/gtfs/reference/#stopstxt
 */
public class Stop {

    /**
     * Identifies a stop, station, or station entrance.
     *
     * The term "station entrance" refers to both station entrances and station exits. Stops, stations or station
     * entrances are collectively referred to as locations. Multiple routes may use the same stop.
     */
    @Getter
    @Setter
    private long id;

    /**
     * Name of the location. Use a name that people will understand in the local and tourist vernacular.
     *
     * When the location is a boarding area (location_type=4), the stop_name should contains the name of the boarding
     * area as displayed by the agency. It could be just one letter (like on some European intercity railway
     * stations), or text like “Wheelchair boarding area” (NYC’s Subway) or “Head of short trains” (Paris’ RER).
     */
    @Getter
    @Setter
    private String name;

    /**
     * Latitude of the location.
     */
    @Getter
    @Setter
    private double latitude;

    /**
     * Longitude of the location.
     */
    @Getter
    @Setter
    private double longitude;

}
