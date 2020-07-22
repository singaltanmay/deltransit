/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * For more information see https://developers.google.com/transit/gtfs/reference/#stop_timestxt
 */
public class StopTime {

    /**
     * Identifies a trip.
     */
    @Getter
    private long tripId;

    /**
     * Arrival time at a specific stop for a specific trip on a route. If there are not separate times for arrival
     * and departure at a stop, enter the same value for arrival_time and departure_time. For times occurring after
     * midnight on the service day, enter the time as a value greater than 24:00:00 in HH:MM:SS local time for the
     * day on which the trip schedule begins.
     */
    @Getter
    private Date arrival;

    /**
     * Departure time from a specific stop for a specific trip on a route. For times occurring after midnight on the
     * service day, enter the time as a value greater than 24:00:00 in HH:MM:SS local time for the day on which the
     * trip schedule begins. If there are not separate times for arrival and departure at a stop, enter the same
     * value for arrival_time and departure_time. See the arrival_time description for more details about using
     * timepoints correctly.
     */
    @Getter
    private Date departure;

    /**
     * Identifies the serviced stop. All stops serviced during a trip must have a record in stop_times.txt.
     * Referenced locations must be stops, not stations or station entrances. A stop may be serviced multiple times
     * in the same trip, and multiple trips and routes may service the same stop.
     */
    @Getter
    private long stopId;

    /**
     * Order of stops for a particular trip. The values must increase along the trip but do not need to be
     * consecutive.Example: The first location on the trip could have a stop_sequence=1, the second location on the
     * trip could have a stop_sequence=23, the third location could have a stop_sequence=40, and so on.
     */
    @Getter
    private long stopSequence;

    public StopTime setTripId(long tripId) {
        this.tripId = tripId;
        return this;
    }

    public StopTime setArrival(Date arrival) {
        this.arrival = arrival;
        return this;
    }

    public StopTime setDeparture(Date departure) {
        this.departure = departure;
        return this;
    }

    public StopTime setStopId(long stopId) {
        this.stopId = stopId;
        return this;
    }

    public StopTime setStopSequence(long stopSequence) {
        this.stopSequence = stopSequence;
        return this;
    }

}
