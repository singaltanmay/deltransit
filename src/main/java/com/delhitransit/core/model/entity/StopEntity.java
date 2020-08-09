/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

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
public class StopEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long key;

    @Getter
    @Setter
    private long stopId;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private double latitude;

    @Getter
    @Setter
    private double longitude;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "stop", cascade = CascadeType.ALL)
    private List<StopTimeEntity> stopTimes;

}
