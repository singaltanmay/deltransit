/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.StopTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
public class StopTimeEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long key;

    @Getter
    @Setter
    private String arrival;

    @Getter
    @Setter
    private String departure;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    private TripEntity trip;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    private StopEntity stop;

    @Getter
    @Setter
    private int stopSequence;

    public StopTimeEntity(StopTime stopTime) {
        this.setArrival(stopTime.getArrival());
        this.setDeparture(stopTime.getDeparture());
        this.setStopSequence(stopTime.getStopSequence());
    }

}
