package com.ceiba.medisalud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ceiba.medisalud.infrastructure.config.MedisaludProperties;

/**
 * Bootstraps the MediSalud appointment scheduling Spring Boot application.
 */
@SpringBootApplication
@EnableConfigurationProperties(MedisaludProperties.class)
public class MedisaludAppointmentsApplication {

    /**
     * Starts the Spring Boot application.
     */
    public static void main(String[] args) {
        SpringApplication.run(MedisaludAppointmentsApplication.class, args);
    }
}
