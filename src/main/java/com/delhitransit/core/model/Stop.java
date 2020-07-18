package com.delhitransit.core.model;

import lombok.Getter;
import lombok.Setter;

public class Stop {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private double latitude;

    @Getter
    @Setter
    private double longitude;

}
