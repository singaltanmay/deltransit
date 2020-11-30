/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.ShapePoint;
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
import java.util.List;

@Entity
@NoArgsConstructor
public class ShapePointEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @JsonIgnore
    private long key;

    @Getter
    @Setter
    private int shapeId;

    @Getter
    @Setter
    private double latitude;

    @Getter
    @Setter
    private double longitude;

    @Getter
    @Setter
    private int sequence;

    @Getter
    @Setter
    @ManyToMany(mappedBy = "shapePoints", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    private List<TripEntity> trips;

    public ShapePointEntity(ShapePoint shapePoint) {
        this.setShapeId(shapePoint.getShapeId());
        this.setLatitude(shapePoint.getLatitude());
        this.setLongitude(shapePoint.getLongitude());
        this.setSequence(shapePoint.getSequence());
    }
}
