package com.bitso.challenge.model.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"com.bitso.challenge.model.db"})
@EntityScan("com.bitso.challenge.entity")
@EnableJpaRepositories("com.bitso.challenge.model.repository")
public class ModelDBConfig {
}
