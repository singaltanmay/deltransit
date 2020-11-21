/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Page<TripEntity> getAllTrips(Pageable request) {
        Page<TripEntity> tripEntityPage = tripRepository.findAll(request);
        return tripEntityPage;
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
