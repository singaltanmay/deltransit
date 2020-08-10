/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitializerService {

    private final RouteService routeService;

    private final StopService stopService;

    private final TripService tripService;

    private final ShapePointService shapePointService;

    @Autowired
    public InitializerService(RouteService routeService, StopService stopService,
                              TripService tripService, ShapePointService shapePointService) {
        this.routeService = routeService;
        this.stopService = stopService;
        this.tripService = tripService;
        this.shapePointService = shapePointService;
    }

    public void setupLinkingForRoutes(){
        List<RouteEntity> allRoutes = routeService.getAllRoutes();
        allRoutes.parallelStream().forEach(
                routeEntity -> {
                }
        );
    }
}
