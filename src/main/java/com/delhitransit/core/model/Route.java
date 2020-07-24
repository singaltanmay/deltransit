/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model;

/**
 * For more information see https://developers.google.com/transit/gtfs/reference/#routestxt
 */
public class Route {

    /**
     * Identifies a route.
     */
    private long routeId;

    /**
     * Short name of a route. This will often be a short, abstract identifier like "32", "100X", or "Green" that
     * riders use to identify a route, but which doesn't give any indication of what places the route serves. Either
     * route_short_name or route_long_name must be specified, or potentially both if appropriate.
     */
    private String shortName;

    /**
     * Full name of a route. This name is generally more descriptive than the route_short_name and often includes the
     * route's destination or stop. Either route_short_name or route_long_name must be specified, or potentially both
     * if appropriate.
     */
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
    private ROUTE_TYPE type = ROUTE_TYPE.BUS;

    /**
     * Agency for the specified route.
     */
    private String agencyId;

    public static ROUTE_TYPE getRouteType(int routeType) {
        switch (routeType) {
            case 0:
                return ROUTE_TYPE.STREET_LEVEL_RAIL;
            case 1:
                return ROUTE_TYPE.SUBWAY;
            case 2:
                return ROUTE_TYPE.RAIL;
            case 4:
                return ROUTE_TYPE.FERRY;
            case 5:
                return ROUTE_TYPE.CABLE_TRAM;
            case 6:
                return ROUTE_TYPE.AERIAL_LIFT;
            case 7:
                return ROUTE_TYPE.FUNICULAR;
            case 8:
                return ROUTE_TYPE.TROLLEYBUS;
            case 9:
                return ROUTE_TYPE.MONORAIL;
            default:
                return ROUTE_TYPE.BUS;
        }
    }

    public long getRouteId() {
        return routeId;
    }

    public Route setRouteId(long routeId) {
        this.routeId = routeId;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public Route setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getLongName() {
        return longName;
    }

    public Route setLongName(String longName) {
        this.longName = longName;
        return this;
    }

    public ROUTE_TYPE getType() {
        return type;
    }

    public Route setType(ROUTE_TYPE type) {
        this.type = type;
        return this;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public Route setAgencyId(String agencyId) {
        this.agencyId = agencyId;
        return this;
    }

    public enum ROUTE_TYPE {
        STREET_LEVEL_RAIL,
        SUBWAY,
        RAIL,
        BUS,
        FERRY,
        CABLE_TRAM,
        AERIAL_LIFT,
        FUNICULAR,
        TROLLEYBUS,
        MONORAIL
    }
}
