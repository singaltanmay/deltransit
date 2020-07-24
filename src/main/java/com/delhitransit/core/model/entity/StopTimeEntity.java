/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalTime;

@Entity
public class StopTimeEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long key;

    @Getter
    @Setter
    private LocalTime arrival;

    @Getter
    @Setter
    private LocalTime departure;

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
    private long stopSequence;

}
