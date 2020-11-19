/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.Route;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.AERIAL_LIFT;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.BUS;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.CABLE_TRAM;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.FERRY;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.FUNICULAR;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.MONORAIL;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.RAIL;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.STREET_LEVEL_RAIL;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.SUBWAY;
import static com.delhitransit.core.model.entity.RouteEntity.ROUTE_TYPE.TROLLEYBUS;
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
    private ROUTE_TYPE type = BUS;

    @Getter
    @Setter
    @OneToMany(mappedBy = "route", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonInclude(NON_NULL)
    private List<TripEntity> trips;

    public RouteEntity(Route route) {
        this.setRouteId(route.getRouteId());
        this.setShortName(route.getShortName());
        this.setLongName(route.getLongName());
        this.setType(getRouteType(route.getType()));
    }

    public static ROUTE_TYPE getRouteType(int routeType) {
        return switch (routeType) {
            case 0 -> STREET_LEVEL_RAIL;
            case 1 -> SUBWAY;
            case 2 -> RAIL;
            case 4 -> FERRY;
            case 5 -> CABLE_TRAM;
            case 6 -> AERIAL_LIFT;
            case 7 -> FUNICULAR;
            case 8 -> TROLLEYBUS;
            case 9 -> MONORAIL;
            default -> BUS;
        };
    }

    public static int getRouteType(ROUTE_TYPE routeType) {
        return switch (routeType) {
            case STREET_LEVEL_RAIL -> 0;
            case SUBWAY -> 1;
            case RAIL -> 2;
            case FERRY -> 4;
            case CABLE_TRAM -> 5;
            case AERIAL_LIFT -> 6;
            case FUNICULAR -> 7;
            case TROLLEYBUS -> 8;
            case MONORAIL -> 9;
            default -> 3;
        };
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
