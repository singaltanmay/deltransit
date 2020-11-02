/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.repository.StopTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopTimeService {

    private final StopTimeRepository stopTimeRepository;

    @Autowired
    public StopTimeService(StopTimeRepository stopTimeRepository) {
        this.stopTimeRepository = stopTimeRepository;
    }

    public List<StopTimeEntity> getAllStopTimes() {
        return stopTimeRepository.findAll();
    }

    public List<StopTimeEntity> getAllStopTimesByStopId(long stopId) {
        return stopTimeRepository.findAllByStop_StopId(stopId);
    }

    public List<StopTimeEntity> getAllStopTimesByTripId(String tripId) {
        return stopTimeRepository.findAllByTrip_TripId(tripId);
    }

    private void insertStopTimes(List<StopTimeEntity> stopTimeEntities) {
        if (stopTimeEntities != null && stopTimeEntities.size() > 0) {
            stopTimeRepository.saveAll(stopTimeEntities);
        }
    }

}
