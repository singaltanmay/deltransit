package com.delhitransit.core.api.controller;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.service.AppService;
import com.delhitransit.core.service.RouteService;
import com.delhitransit.core.service.ShapePointService;
import com.delhitransit.core.service.StopService;
import com.delhitransit.core.service.StopTimeService;
import com.delhitransit.core.service.TripService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1")
public class DelhiTransitController {

    public static final int DEFAULT_PAGE_NUMBER = 0;

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);

    private final RouteService routeService;

    private final TripService tripService;

    private final ShapePointService shapePointService;

    private final StopService stopService;

    private final StopTimeService stopTimeService;

    private final AppService appService;

    public static final String PAGE_NUMBER_DESCRIPTION = "You can provide a page index to return only a subset of" +
            " the data. Page numbers start from 0 and if not specified then default page number is 0.";

    public static final String PAGE_SIZE_DESCRIPTION = "You can provide a page size to return only a subset of"
            + " the data. Page size should be greater than 0 and if not specified then default page size is 10 " +
            "results per page.";

    @Autowired
    public DelhiTransitController(RouteService routeService, TripService tripService,
                                  ShapePointService shapePointService, StopService stopService,
                                  StopTimeService stopTimeService, AppService appService) {
        this.routeService = routeService;
        this.tripService = tripService;
        this.shapePointService = shapePointService;
        this.stopService = stopService;
        this.stopTimeService = stopTimeService;
        this.appService = appService;
    }

    public static PageRequest createPageRequest(Integer page, Integer size) {

        int pagex = DEFAULT_PAGE_NUMBER, sizex = DEFAULT_PAGE_SIZE;

        if (page != null) {
            pagex = page;
        }
        if (size != null) {
            sizex = size;
        }

        if (pagex == DEFAULT_PAGE_NUMBER && sizex == DEFAULT_PAGE_SIZE) {
            return DEFAULT_PAGE_REQUEST;
        } else {
            return PageRequest.of(pagex, sizex);
        }
    }

    public static boolean isPageParamsValid(Integer page, Integer size) {
        return (page == null || page >= 0) && (size == null || size >= 0);
    }

    public static <T> ResponseEntity<List<T>> createAppropriateResponseEntity(Page<T> page) {
        if (page != null && !page.isEmpty()) {
            return new ResponseEntity<>(page.toList(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("routes")
    public ResponseEntity<List<RouteEntity>> getAllRoutes(
        @RequestParam(required = false, name = "page") @ApiParam(value = PAGE_NUMBER_DESCRIPTION) Integer pageNumber,
        @RequestParam(required = false, name = "size") @ApiParam(value = PAGE_SIZE_DESCRIPTION) Integer pageSize)
    {
        if(!isPageParamsValid(pageNumber, pageSize)) return new ResponseEntity<>(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        return createAppropriateResponseEntity(routeService.getAllRoutes(createPageRequest(pageNumber, pageSize)));
    }

    @GetMapping("routes/id/{id}")
    public List<RouteEntity> getRoutesByRouteId(@PathVariable("id") long routeId) {
        return routeService.getRoutesByRouteId(routeId);
    }

    @GetMapping("routes/name/{name}")
    public List<RouteEntity> getRoutesByRouteName(
            @PathVariable("name") String name,
            @RequestParam(name = "exact") Optional<Boolean> matchType,
            @RequestParam(name = "short") Optional<Boolean> nameType) {
        boolean isExactMatch = matchType != null && matchType.isPresent() && matchType.get();
        boolean isShortName = nameType != null && nameType.isPresent() && nameType.get();
        if (isShortName) {
            if (isExactMatch) {
                return routeService.getRoutesByShortNameIgnoreCase(name);
            } else {
                return routeService.getRoutesByShortNameContainsIgnoreCase(name);
            }
        } else {
            if (isExactMatch) {
                return routeService.getRoutesByLongNameIgnoreCase(name);
            } else {
                return routeService.getRoutesByLongNameContainsIgnoreCase(name);
            }
        }
    }

    @GetMapping("routes/type/{type}")
    public List<RouteEntity> getRoutesByRouteType(@PathVariable("type") int type) {
        return routeService.getRoutesByType(type);
    }

    @GetMapping("routes/between")
    public List<RouteEntity> getRoutesBetweenStops(@RequestParam long source, @RequestParam long destination) {
        return appService.getRoutesBetweenTwoStops(source, destination);
    }

    @GetMapping("trips")
    public List<TripEntity> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("shapePoints")
    public List<ShapePointEntity> getAllShapePoints() {
        return shapePointService.getAllShapePoints();
    }

    @GetMapping("shapePoints/trip/{trip}")
    public List<ShapePointEntity> getAllShapePointsByTripId(@PathVariable(name = "trip") String tripId) {
        return shapePointService.getAllShapePointsByTripId(tripId);
    }

    @GetMapping("stops")
    public List<StopEntity> getAllStops() {
        return stopService.getAllStops();
    }

    @GetMapping("stops/name/{name}")
    public List<StopEntity> getStopsByName(
            @PathVariable String name,
            @RequestParam(name = "exact") Optional<Boolean> matchType) {
        boolean isExactMatch = matchType != null && matchType.isPresent() && matchType.get();
        if (isExactMatch) {
            return stopService.getStopsByNameIgnoreCase(name);
        } else {
            return stopService.getStopsByNameContainsIgnoreCase(name);
        }
    }

    @GetMapping("stops/nearby")
    public List<StopEntity> getStopsNearLocation(
            @RequestParam(name = "lat") Optional<Double> latitude,
            @RequestParam(name = "lon") Optional<Double> longitude,
            @RequestParam(name = "dist", required = false) Optional<Double> distance) {
        if (latitude != null && latitude.isPresent() && longitude != null && longitude.isPresent()) {
            return stopService.getStopsNearLocation(latitude.get(), longitude.get(), distance.orElse(null));
        } else return Collections.emptyList();
    }

    @GetMapping("stopTimes")
    public List<StopTimeEntity> getAllStopTimes() {
        return stopTimeService.getAllStopTimes();
    }

}
