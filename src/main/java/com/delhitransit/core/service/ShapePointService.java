/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.ShapePointEntity;
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

    private void insertTrips(List<ShapePointEntity> shapePointEntities) {
        if (shapePointEntities != null && shapePointEntities.size() > 0) {
            shapePointRepository.saveAll(shapePointEntities);
        }
    }

}
