package com.delhitransit.core.controller;

import com.delhitransit.core.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("delhitransit-admin/v1")
public class AdminController {

    private RouteService routeService;

    @Autowired
    public AdminController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("init/routes")
    public void initializeRoutesTable() throws IOException {
        routeService.initializeUnlinkedDatabase();
    }
}
