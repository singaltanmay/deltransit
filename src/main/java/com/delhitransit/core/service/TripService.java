/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TripService {

    TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Page<TripEntity> getAllTrips(Pageable request) {
        return tripRepository.findAll(request);
    }

    public TripEntity getTripByTripId(String tripId) {
        return tripRepository.findFirstByTripId(tripId);
    }

    public TripEntity getTripByRouteId(long routeId){
        return tripRepository.findFirstByRoute_RouteId(routeId);
    }

    public Long getTripTravelTimeBetweenTwoStops(String tripId, long source, long destination) {
        TripEntity trip = getTripByTripId(tripId);
        AtomicLong sourceStopTime = new AtomicLong();
        AtomicLong destinationStopTime = new AtomicLong();
        trip.getStopTimes().forEach(it -> {
            StopEntity stop = it.getStop();
            if (stop.getStopId() == source) sourceStopTime.set(it.getArrival());
            if (stop.getStopId() == destination) destinationStopTime.set(it.getArrival());
        });
        return destinationStopTime.get() - sourceStopTime.get();
    }

}
