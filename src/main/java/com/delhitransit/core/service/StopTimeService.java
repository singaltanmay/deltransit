/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.model.parseable.StopTime;
import com.delhitransit.core.reader.StopTimeReader;
import com.delhitransit.core.repository.StopTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StopTimeService {

    StopTimeRepository stopTimeRepository;

    @Autowired
    public StopTimeService(StopTimeRepository stopTimeRepository) {
        this.stopTimeRepository = stopTimeRepository;
    }

    public List<StopTimeEntity> getAllStopTimes() {
        return stopTimeRepository.findAll();
    }

    public void initializeUnlinkedDatabase() throws IOException {
        List<StopTimeEntity> stopTimeEntities = parseCsvToEntityList();
        insertStopTimes(stopTimeEntities);
    }

    private List<StopTimeEntity> parseCsvToEntityList() throws IOException {

        List<StopTimeEntity> stopTimeEntities = new ArrayList<>();

        List<StopTime> stopTimes = new StopTimeReader().read();
        for (StopTime stopTime : stopTimes) {
            stopTimeEntities.add(new StopTimeEntity(stopTime));
        }

        return stopTimeEntities;

    }

    private void insertStopTimes(List<StopTimeEntity> stopTimeEntities) {
        if (stopTimeEntities != null && stopTimeEntities.size() > 0) {
            stopTimeRepository.saveAll(stopTimeEntities);
        }
    }

}
