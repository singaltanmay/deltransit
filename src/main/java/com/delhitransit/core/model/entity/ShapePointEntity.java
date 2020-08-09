/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.ShapePoint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
public class ShapePointEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shapePoint", cascade = CascadeType.ALL)
    private List<TripEntity> trips;

    public ShapePointEntity(ShapePoint shapePoint) {
        this.setShapeId(shapePoint.getShapeId());
        this.setLatitude(shapePoint.getLatitude());
        this.setLongitude(shapePoint.getLongitude());
        this.setSequence(shapePoint.getSequence());
    }
}
