/*
 * @author Tanmay Singal
 */

package com.delhitransit.core;

import com.delhitransit.core.service.InitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class CoreApplication {

    private static InitializerService service;

    @Autowired
    public CoreApplication(InitializerService service) {
        CoreApplication.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
        service.init(Optional.empty());
    }

}
