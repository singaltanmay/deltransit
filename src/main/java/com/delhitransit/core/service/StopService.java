/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.StopEntity;
import com.delhitransit.core.model.parseable.Stop;
import com.delhitransit.core.reader.StopReader;
import com.delhitransit.core.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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

    public void initializeUnlinkedDatabase() throws IOException {
        List<StopEntity> stopEntities = parseCsvToEntityList();
        insertStops(stopEntities);
    }

    private List<StopEntity> parseCsvToEntityList() throws IOException {

        List<StopEntity> stopEntities = new ArrayList<>();

        List<Stop> stops = new StopReader().read();
        for (Stop stop : stops) {
            stopEntities.add(new StopEntity(stop));
        }

        return stopEntities;

    }

    private void insertStops(List<StopEntity> stopEntities) {
        if (stopEntities != null && stopEntities.size() > 0) {
            stopRepository.saveAll(stopEntities);
        }
    }

}
