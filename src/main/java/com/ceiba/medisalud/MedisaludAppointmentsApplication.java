package com.ceiba.medisalud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ceiba.medisalud.infrastructure.config.MedisaludProperties;

@SpringBootApplication
@EnableConfigurationProperties(MedisaludProperties.class)
public class MedisaludAppointmentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedisaludAppointmentsApplication.class, args);
    }
}
