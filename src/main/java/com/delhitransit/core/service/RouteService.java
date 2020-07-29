/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.service;

import com.delhitransit.core.model.entity.RouteEntity;
import com.delhitransit.core.model.parseable.Route;
import com.delhitransit.core.reader.RouteReader;
import com.delhitransit.core.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class RouteService {

    RouteRepository repository;

    @Autowired
    public RouteService(RouteRepository repository) {
        this.repository = repository;
    }

    public List<RouteEntity> getAllRoutes(){
        return repository.findAll();
    }

    public void initializeUnlinkedDatabase() throws IOException {
        List<RouteEntity> entities = parseCsvToEntityList();
        insertRoutes(entities);
    }

    private List<RouteEntity> parseCsvToEntityList() throws IOException {

        List<RouteEntity> entities = new LinkedList<>();

        List<Route> routes = new RouteReader().read();
        for (Route route : routes) {
            entities.add(new RouteEntity(route));
        }

        return entities;

    }

    private void insertRoutes(List<RouteEntity> routes) {
        if (routes != null && routes.size() > 0) {
            repository.saveAll(routes);
        }
    }

}
