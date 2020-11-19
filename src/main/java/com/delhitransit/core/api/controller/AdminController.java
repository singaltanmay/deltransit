package com.delhitransit.core.api.controller;

import com.delhitransit.core.service.InitializerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("v1/admin")
public class AdminController {

    private final InitializerService initializerService;

    @Autowired
    public AdminController(InitializerService initializerService) {
        this.initializerService = initializerService;
    }

    @ApiOperation(value = "Provide the base url of the OTD Parser API. For example :- \"https://otd-parser.herokuapp" +
            ".com/v1/\". This endpoint should not be used if the database has already been partially initialized by " +
            "another URL or default value.")
    @PostMapping("init")
    public void initializeDatabase(
            @RequestParam(name = "otd", required = false, defaultValue = "https://otd-parser.herokuapp.com/v1/") Optional<String> otdUrl) {
        initializerService.init(otdUrl);
    }

}
