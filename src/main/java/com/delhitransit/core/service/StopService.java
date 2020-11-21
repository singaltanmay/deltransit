/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.GeoLocationHelper;
import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopService {

    StopRepository stopRepository;

    @Autowired
    public StopService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public Page<StopEntity> getAllStops(Pageable request) {
        Page<StopEntity> stopEntityPage = stopRepository.findAll(request);
        stopEntityPage.forEach(this::removeStopTimesFromStop);
        return stopEntityPage;
    }

    public Page<StopEntity> getStopsByNameIgnoreCase(String name, Pageable request) {
        Page<StopEntity> stopEntityPage = stopRepository.findAllByNameIgnoreCase(name, request);
        stopEntityPage.forEach(this::removeStopTimesFromStop);
        return  stopEntityPage;
    }

    public Page<StopEntity> getStopsByNameContainsIgnoreCase(String preStopName, Pageable request) {
        Page<StopEntity> stopEntityPage = stopRepository.findAllByNameContainsIgnoreCase(preStopName, request);
        stopEntityPage.forEach(this::removeStopTimesFromStop);
        return stopEntityPage;
    }

    public Page<StopEntity> getStopsNearLocation(double myLat, double myLon, Double distance, Pageable request) {
        GeoLocationHelper location = GeoLocationHelper.fromDegrees(myLat, myLon);
        GeoLocationHelper[] coordinates = location.boundingCoordinates(distance != null ? distance : 1L, null);
        Page<StopEntity> stopEntityPage = stopRepository
                .findAllByLatitudeBetweenAndLongitudeBetween(
                        coordinates[0].getLatitudeInDegrees(),
                        coordinates[1].getLatitudeInDegrees(),
                        coordinates[0].getLongitudeInDegrees(),
                        coordinates[1].getLongitudeInDegrees(),
                        request);
        stopEntityPage.forEach(this::removeStopTimesFromStop);
        return stopEntityPage;
    }

    private List<StopEntity> removeStopTimesFromStops(List<StopEntity> stops) {
        if (stops != null && stops.size() > 0) {
            for (StopEntity stop : stops) {
                stop.setStopTimes(null);
            }
        }
        return stops;
    }

    private StopEntity removeStopTimesFromStop(StopEntity stop) {
        if (stop != null) {
            stop.setStopTimes(null);
        }
        return stop;
    }

    private void insertStops(List<StopEntity> stopEntities) {
        if (stopEntities != null && stopEntities.size() > 0) {
            stopRepository.saveAll(stopEntities);
        }
    }

}
