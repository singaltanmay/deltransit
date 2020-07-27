/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.parseable;

/**
 * For more information see https://developers.google.com/transit/gtfs/reference/#stop_timestxt
 */
public class StopTime {

    /**
     * Identifies a trip.
     */
    private String tripId;

    /**
     * Arrival time at a specific stop for a specific trip on a route. If there are not separate times for arrival
     * and departure at a stop, enter the same value for arrival_time and departure_time. For times occurring after
     * midnight on the service day, enter the time as a value greater than 24:00:00 in HH:MM:SS local time for the
     * day on which the trip schedule begins.
     */
    private long arrival;

    /**
     * Departure time from a specific stop for a specific trip on a route. For times occurring after midnight on the
     * service day, enter the time as a value greater than 24:00:00 in HH:MM:SS local time for the day on which the
     * trip schedule begins. If there are not separate times for arrival and departure at a stop, enter the same
     * value for arrival_time and departure_time. See the arrival_time description for more details about using
     * timepoints correctly.
     */
    private long departure;

    /**
     * Identifies the serviced stop. All stops serviced during a trip must have a record in stop_times.txt.
     * Referenced locations must be stops, not stations or station entrances. A stop may be serviced multiple times
     * in the same trip, and multiple trips and routes may service the same stop.
     */
    private long stopId;

    /**
     * Order of stops for a particular trip. The values must increase along the trip but do not need to be
     * consecutive.Example: The first location on the trip could have a stop_sequence=1, the second location on the
     * trip could have a stop_sequence=23, the third location could have a stop_sequence=40, and so on.
     */
    private long stopSequence;

    public StopTime setTripId(String tripId) {
        this.tripId = tripId;
        return this;
    }

    public String getTripId() {
        return tripId;
    }

    public StopTime setArrival(String arrival) {
        this.arrival = Long.parseLong(arrival.substring(0, 2)) * 3600 + Long.parseLong(
                arrival.substring(3, 5)) * 60 + Long.parseLong(arrival.substring(6, 8));
        return this;
    }

    public String getArrival() {
        return longToTimeString(arrival);
    }

    public StopTime setDeparture(String departure) {
        this.departure = Long.parseLong(departure.substring(0, 2)) * 3600 + Long.parseLong(
                departure.substring(3, 5)) * 60 + Long.parseLong(departure.substring(6, 8));
        return this;
    }

    public String getDeparture() {
        return longToTimeString(departure);
    }

    public StopTime setStopId(long stopId) {
        this.stopId = stopId;
        return this;
    }

    public long getStopId() {
        return stopId;
    }

    public long getStopSequence() {
        return stopSequence;
    }

    public StopTime setStopSequence(long stopSequence) {
        this.stopSequence = stopSequence;
        return this;
    }

    private String longToTimeString(long input_secs) {
        long[] time_arr = new long[3];
        long hrs = time_arr[0] = input_secs / 3600;
        long mins = time_arr[1] = (input_secs - hrs * 3600) / 60;
        time_arr[2] = input_secs - hrs * 3600 - mins * 60;
        String time_string = "";
        for (long elem : time_arr) {
            if (elem / 10 < 1) time_string += "0" + elem + ":";
            else time_string += elem + ":";
        }
        time_string = time_string.substring(0, time_string.length() - 1);
        return time_string;
    }
}
