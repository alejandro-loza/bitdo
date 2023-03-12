package com.bitso.challenge.service;

import com.bitso.challenge.model.config.ModelDBConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Entry point and configuration provider.
 */
@SpringBootApplication
@Import({ModelDBConfig.class})
public class ServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
