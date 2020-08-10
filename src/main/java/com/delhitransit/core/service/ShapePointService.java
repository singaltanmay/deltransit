/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.ShapePointEntity;
import com.delhitransit.core.model.parseable.ShapePoint;
import com.delhitransit.core.reader.ShapePointReader;
import com.delhitransit.core.repository.ShapePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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

    public void initializeUnlinkedDatabase() throws IOException {
        List<ShapePointEntity> shapePointEntities = parseCsvToEntityList() ;
        insertTrips(shapePointEntities);
    }

    private List<ShapePointEntity> parseCsvToEntityList() throws IOException {

        List<ShapePointEntity> shapePointEntities = new ArrayList<>();

        List<ShapePoint> shapePoints = new ShapePointReader().read();
        for (ShapePoint shapePoint : shapePoints) {
            shapePointEntities.add(new ShapePointEntity(shapePoint));
        }

        return shapePointEntities;

    }

    private void insertTrips(List<ShapePointEntity> shapePointEntities) {
        if(shapePointEntities != null && shapePointEntities.size() > 0) {
            shapePointRepository.saveAll(shapePointEntities);
        }
    }

}
