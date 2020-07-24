/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long pKey;

    private String shortName;

    private String longName;

    private ROUTE_TYPE type = ROUTE_TYPE.BUS;

    public long getpKey() {
        return pKey;
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
