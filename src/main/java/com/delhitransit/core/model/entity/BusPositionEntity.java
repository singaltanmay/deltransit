/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.GtfsRealtime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BusPositionEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @JsonIgnore
    private long key;

    @Getter
    private String vehicleID;

    @Getter
    private String tripID;

    @Getter
    private String routeID;

    @Getter
    private float latitude;

    @Getter
    private float longitude;

    @Getter
    private float speed;

    @Getter
    private long timestamp;

    public BusPositionEntity parseFrom(GtfsRealtime.FeedEntity entity){
        this.vehicleID = entity.getId();

        GtfsRealtime.VehiclePosition vehicle = entity.getVehicle();
        GtfsRealtime.TripDescriptor trip = vehicle.getTrip();
        GtfsRealtime.Position position = vehicle.getPosition();

        this.tripID = trip.getTripId();
        this.routeID = trip.getRouteId();
        this.latitude = position.getLatitude();
        this.longitude = position.getLongitude();
        this.speed = position.getSpeed();
        this.timestamp = vehicle.getTimestamp();
        return this;
    }
}