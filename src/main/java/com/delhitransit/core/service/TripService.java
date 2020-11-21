/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<TripEntity> getAllTrips() {
        return tripRepository.findAll();
    }

    private void insertTrips(List<TripEntity> tripEntities) {
        if (tripEntities != null && tripEntities.size() > 0) {
            tripRepository.saveAll(tripEntities);
        }
    }

    public TripEntity getTripByTripId(String tripId) {
        return tripRepository.findFirstByTripId(tripId);
    }
}
