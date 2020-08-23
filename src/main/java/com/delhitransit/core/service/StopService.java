/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

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
        return stopRepository.findAll();
    }

    public List<StopEntity> getStopsByName(String stopName) {
        return stopRepository.findByName(stopName);
    }

    private void insertStops(List<StopEntity> stopEntities) {
        if (stopEntities != null && stopEntities.size() > 0) {
            stopRepository.saveAll(stopEntities);
        }
    }

}
