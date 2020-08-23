/*
 * @author Tanmay Singal
 */

package com.delhitransit.core;

import com.delhitransit.core.service.InitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CoreApplication {

    static InitializerService service;

    @Autowired
    public CoreApplication(InitializerService service) {
        CoreApplication.service = service;
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CoreApplication.class, args);
        service.init();
    }

}
