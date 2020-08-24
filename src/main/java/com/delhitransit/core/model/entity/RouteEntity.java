/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.Route;
import com.fasterxml.jackson.annotation.JsonInclude;
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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Entity
@NoArgsConstructor
public class RouteEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long key;

    @Getter
    @Setter
    private long routeId;

    @Getter
    @Setter
    private String shortName;

    @Getter
    @Setter
    private String longName;

    @Getter
    @Setter
    private ROUTE_TYPE type = ROUTE_TYPE.BUS;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "route", cascade = CascadeType.REMOVE)
    @JsonInclude(NON_NULL)
    private List<TripEntity> trips;

    public RouteEntity(Route route) {
        this.setRouteId(route.getRouteId());
        this.setShortName(route.getShortName());
        this.setLongName(route.getLongName());
        this.setType(getRouteType(route.getType()));
    }

    public static ROUTE_TYPE getRouteType(int routeType) {
        switch (routeType) {
            case 0:
                return ROUTE_TYPE.STREET_LEVEL_RAIL;
            case 1:
                return ROUTE_TYPE.SUBWAY;
            case 2:
                return ROUTE_TYPE.RAIL;
            case 4:
                return ROUTE_TYPE.FERRY;
            case 5:
                return ROUTE_TYPE.CABLE_TRAM;
            case 6:
                return ROUTE_TYPE.AERIAL_LIFT;
            case 7:
                return ROUTE_TYPE.FUNICULAR;
            case 8:
                return ROUTE_TYPE.TROLLEYBUS;
            case 9:
                return ROUTE_TYPE.MONORAIL;
            default:
                return ROUTE_TYPE.BUS;
        }
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
