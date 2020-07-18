package com.delhitransit.core.model;

import lombok.Getter;
import lombok.Setter;

public class Route {

    public enum ROUTE_TYPE{
        BUS
    }

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
    private long id;

    @Getter
    @Setter
    private long agencyId;

}
