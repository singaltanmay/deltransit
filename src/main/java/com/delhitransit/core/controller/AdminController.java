package com.delhitransit.core.controller;

import com.delhitransit.core.service.InitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("v1/admin")
public class AdminController {

    private final InitializerService initializerService;

    @Autowired
    public AdminController(InitializerService initializerService) {
        this.initializerService = initializerService;
    }

    @PostMapping("init")
    public void initializeDatabase() throws IOException {
        initializerService.init();
    }

}
