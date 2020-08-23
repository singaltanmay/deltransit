/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.parseable;

import lombok.Getter;

/**
 * For more information see https://developers.google.com/transit/gtfs/reference/#routestxt
 */
public class Route {

    /**
     * Identifies a route.
     */
    @Getter
    private long routeId;

    /**
     * Short name of a route. This will often be a short, abstract identifier like "32", "100X", or "Green" that
     * riders use to identify a route, but which doesn't give any indication of what places the route serves. Either
     * route_short_name or route_long_name must be specified, or potentially both if appropriate.
     */
    @Getter
    private String shortName;

    /**
     * Full name of a route. This name is generally more descriptive than the route_short_name and often includes the
     * route's destination or stop. Either route_short_name or route_long_name must be specified, or potentially both
     * if appropriate.
     */
    @Getter
    private String longName;

    /**
     * Indicates the type of transportation used on a route. Valid options are:
     * 0 - Tram, Streetcar, Light rail. Any light rail or street level system within a metropolitan area.
     * 1 - Subway, Metro. Any underground rail system within a metropolitan area.
     * 2 - Rail. Used for intercity or long-distance travel.
     * 3 - Bus. Used for short- and long-distance bus routes.
     * 4 - Ferry. Used for short- and long-distance boat service.
     * 5 - Cable tram. Used for street-level rail cars where the cable runs beneath the vehicle, e.g., cable car in
     * San Francisco.
     * 6 - Aerial lift, suspended cable car (e.g., gondola lift, aerial tramway). Cable transport where cabins, cars,
     * gondolas or open chairs are suspended by means of one or more cables.
     * 7 - Funicular. Any rail system designed for steep inclines.
     * 11 - Trolleybus. Electric buses that draw power from overhead wires using poles.
     * 12 - Monorail. Railway in which the track consists of a single rail or a beam.
     */
    @Getter
    private int type;

    /**
     * Agency for the specified route.
     */
    @Getter
    private String agencyId;

    public Route setRouteId(long routeId) {
        this.routeId = routeId;
        return this;
    }

    public Route setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public Route setLongName(String longName) {
        this.longName = longName;
        return this;
    }

    public Route setType(int type) {
        this.type = type;
        return this;
    }

    public Route setAgencyId(String agencyId) {
        this.agencyId = agencyId;
        return this;
    }

}
