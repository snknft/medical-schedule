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
     * Provides the application clock used by time-sensitive rules.
     *
     * @return system default zone clock
     */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
