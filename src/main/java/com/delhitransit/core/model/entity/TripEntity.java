/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.Trip;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
public class TripEntity {

    @Getter
    @Setter
    @OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<StopTimeEntity> stopTimes;

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long key;

    @Getter
    @Setter
    private String tripId;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    private RouteEntity route;

    @Getter
    @Setter
    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    private List<ShapePointEntity> shapePoints;

    public TripEntity(Trip trip) {
        this.setTripId(trip.getTripId());
    }
}
