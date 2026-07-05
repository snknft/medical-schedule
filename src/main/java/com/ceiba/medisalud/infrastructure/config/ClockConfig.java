package com.ceiba.medisalud.infrastructure.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the application clock used by time-sensitive business rules.
 */
@Configuration
public class ClockConfig {

    /**
     * Executes the clock operation.
     */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
