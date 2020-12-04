/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.repository.ShapePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ShapePointService {

    ShapePointRepository shapePointRepository;

    @Autowired
    public ShapePointService(ShapePointRepository shapePointRepository) {
        this.shapePointRepository = shapePointRepository;
    }

    public Page<ShapePointEntity> getAllShapePoints(Pageable request) {
        Page<ShapePointEntity> shapePointEntityPage = shapePointRepository.findAll(request);
        return shapePointEntityPage;
    }

    public static List<ShapePointEntity> sortShapePointsBySequence(List<ShapePointEntity> shapePoints) {
        shapePoints.sort(Comparator.comparingInt(ShapePointEntity::getSequence));
        return shapePoints;
    }

}
