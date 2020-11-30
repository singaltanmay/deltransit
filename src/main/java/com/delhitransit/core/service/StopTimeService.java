/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.repository.StopTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class StopTimeService {

    private final StopTimeRepository stopTimeRepository;

    @Autowired
    public StopTimeService(StopTimeRepository stopTimeRepository) {
        this.stopTimeRepository = stopTimeRepository;
    }

    public Page<StopTimeEntity> getAllStopTimes(Pageable request) {
        Page<StopTimeEntity> page = stopTimeRepository.findAll(request);
        return page;
    }

    public List<StopTimeEntity> getAllStopTimesByStopId(long stopId) {
        return stopTimeRepository.findAllByStop_StopId(stopId);
    }

    public List<StopTimeEntity> getAllStopTimesByStopIdAndArrivalTimeAfter(long stopId, long earliestArrivalTime) {
        return stopTimeRepository.findAllByStop_StopIdAndArrivalAfterOrderByArrival(stopId, earliestArrivalTime);
    }

    public static List<StopTimeEntity> sortStopTimesByStopArrivalTime(List<StopTimeEntity> stopTimes) {
        stopTimes.sort(Comparator.comparingLong(StopTimeEntity::getArrival));
        return stopTimes;
    }
}
