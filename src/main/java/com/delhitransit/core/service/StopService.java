/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.GeoLocationHelper;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopService {

    StopRepository stopRepository;

    @Autowired
    public StopService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public List<StopEntity> getAllStops() {
        return removeStopTimesFromStops(stopRepository.findAll());
    }

    public List<StopEntity> getStopsByNameIgnoreCase(String name) {
        return removeStopTimesFromStops(stopRepository.findAllByNameIgnoreCase(name));
    }

    public List<StopEntity> getStopsByNameContainsIgnoreCase(String preStopName) {
        return removeStopTimesFromStops(stopRepository.findAllByNameContainsIgnoreCase(preStopName));
    }

    public List<StopEntity> getStopsNearLocation(double myLat, double myLon, Double distance) {
        GeoLocationHelper location = GeoLocationHelper.fromDegrees(myLat, myLon);
        GeoLocationHelper[] coordinates = location.boundingCoordinates(distance != null ? distance : 1L, null);
        List<StopEntity> entities = stopRepository
                .findAllByLatitudeBetweenAndLongitudeBetween(
                        coordinates[0].getLatitudeInDegrees(),
                        coordinates[1].getLatitudeInDegrees(),
                        coordinates[0].getLongitudeInDegrees(),
                        coordinates[1].getLongitudeInDegrees());
        return removeStopTimesFromStops(entities);
    }

    private List<StopEntity> removeStopTimesFromStops(List<StopEntity> stops) {
        if (stops != null && stops.size() > 0) {
            for (StopEntity stop : stops) {
                stop.setStopTimes(null);
            }
        }
        return stops;
    }

    private void insertStops(List<StopEntity> stopEntities) {
        if (stopEntities != null && stopEntities.size() > 0) {
            stopRepository.saveAll(stopEntities);
        }
    }

}
