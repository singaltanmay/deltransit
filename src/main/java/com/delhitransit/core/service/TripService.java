/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.model.parseable.Trip;
import com.delhitransit.core.reader.TripReader;
import com.delhitransit.core.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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

    public List<TripEntity> getAllTripsByRouteId(long routeId) {
        return tripRepository.findTripEntitiesByRoute_RouteId(routeId);
    }

    public void initializeUnlinkedDatabase() throws IOException {
        List<TripEntity> tripEntities = parseCsvToEntityList();
        insertTrips(tripEntities);
    }

    private List<TripEntity> parseCsvToEntityList() throws IOException {

        List<TripEntity> tripEntities = new ArrayList<>();

        List<Trip> trips = new TripReader().read();
        for (Trip trip : trips) {
            tripEntities.add(new TripEntity(trip));
        }

        return tripEntities;

    }

    private void insertTrips(List<TripEntity> tripEntities) {
        if (tripEntities != null && tripEntities.size() > 0) {
            tripRepository.saveAll(tripEntities);
        }
    }

}
