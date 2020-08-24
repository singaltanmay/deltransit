/*
 * @author Tanmay Singal
 */

package com.delhitransit.core;

import com.delhitransit.core.model.entity.RouteEntity;

import java.util.LinkedList;
import java.util.Random;

public class EntityGenerator {

    public static class RouteEntityGenerator {

        public static RouteEntity generate() {
            RouteEntity routeEntity = new RouteEntity();
            long randomLong = new Random().nextLong();
            routeEntity.setRouteId(randomLong);
            String randomLongString = Long.toString(randomLong);
            routeEntity.setShortName("short name " + randomLongString);
            routeEntity.setLongName("long name " + randomLongString);
            routeEntity.setType(RouteEntity.getRouteType(new Random().nextInt(10)));
            routeEntity.setTrips(new LinkedList<>());
            return routeEntity;
        }

    }

}
