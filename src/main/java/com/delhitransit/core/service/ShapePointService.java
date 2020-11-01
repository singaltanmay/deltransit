/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.entity.TripEntity;
import com.delhitransit.core.repository.ShapePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShapePointService {

    ShapePointRepository shapePointRepository;

    @Autowired
    public ShapePointService(ShapePointRepository shapePointRepository) {
        this.shapePointRepository = shapePointRepository;
    }

    public List<ShapePointEntity> getAllShapePoints() {
        return shapePointRepository.findAll();
    }

    private void insertShapePoints(List<ShapePointEntity> shapePointEntities) {
        if (shapePointEntities != null && shapePointEntities.size() > 0) {
            shapePointRepository.saveAll(shapePointEntities);
        }
    }

    public List<ShapePointEntity> getAllShapePointsByTripId(String tripId) {
        List<ShapePointEntity> entities = shapePointRepository.findAll();
        for (ShapePointEntity entity : entities) {
            boolean contains = false;
            for (TripEntity trip : entity.getTrips()) {
                if (trip.getTripId().equals(tripId)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) entities.remove(entity);
        }
        return entities;
    }
}
