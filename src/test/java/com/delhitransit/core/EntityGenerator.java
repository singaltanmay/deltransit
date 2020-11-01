/*
 * @author Tanmay Singal
 */

package com.delhitransit.core;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.TripEntity;

import java.util.LinkedList;
import java.util.Random;

public class EntityGenerator {

    public static class RouteEntityGenerator {

        public static RouteEntity generate() {
            RouteEntity routeEntity = new RouteEntity();
            Random random = new Random();
            long randomLong = random.nextLong();
            routeEntity.setRouteId(randomLong);
            String randomLongString = Long.toString(randomLong);
            routeEntity.setShortName("short name " + randomLongString);
            routeEntity.setLongName("long name " + randomLongString);
            routeEntity.setType(RouteEntity.getRouteType(random.nextInt(10)));
            routeEntity.setTrips(new LinkedList<>());
            return routeEntity;
        }
    }

    public static class StopEntityGenerator {

        public static StopEntity generate() {
            StopEntity stopEntity = new StopEntity();
            Random random = new Random();
            long randomLong = random.nextLong();
            String randomLongString = Long.toString(randomLong);
            double randomDouble = random.nextDouble();
            stopEntity.setStopId(randomLong);
            stopEntity.setName("Stop Name : " + randomLongString);
            stopEntity.setLongitude(randomDouble);
            stopEntity.setLatitude(randomDouble);
            stopEntity.setStopTimes(new LinkedList<>());
            return stopEntity;
        }
    }

    public static class ShapePointEntityGenerator {

        public static ShapePointEntity generate() {
            ShapePointEntity shapePointEntity = new ShapePointEntity();
            Random random = new Random();
            shapePointEntity.setLatitude(random.nextDouble());
            shapePointEntity.setLongitude(random.nextDouble());
            shapePointEntity.setSequence(random.nextInt());
            shapePointEntity.setShapeId(random.nextInt());
            shapePointEntity.setTrips(new LinkedList<>());
            return shapePointEntity;
        }
    }

    public static class TripEntityGenerator {

        public static TripEntity generate() {
            TripEntity tripEntity = new TripEntity();
            Random random = new Random();
            tripEntity.setTripId("trip" + random.nextLong());
            tripEntity.setStopTimes(new LinkedList<>());
            tripEntity.setShapePoints(new LinkedList<>());
            return tripEntity;
        }
    }

}
