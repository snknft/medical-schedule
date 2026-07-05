package com.ceiba.medisalud.infrastructure.config;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the application clock used by time-sensitive business rules.
 */
@Configuration
public class ClockConfig {

    /**
     * Creates the application clock.
     *
     * <p>By default, the system clock is used. For local demonstrations or automated tests,
     * a fixed date-time can be configured with the {@code medisalud.clock.fixed-now}
     * property using ISO-8601 local date-time format.</p>
     *
     * @param fixedNow optional fixed date-time used to override the system clock
     * @return the configured application clock
     */
    @Bean
    public Clock clock(@Value("${medisalud.clock.fixed-now:}") String fixedNow) {
        ZoneId zone = ZoneId.systemDefault();

        if (fixedNow == null || fixedNow.isBlank()) {
            return Clock.system(zone);
        }

        LocalDateTime fixedDateTime = LocalDateTime.parse(fixedNow);
        return Clock.fixed(fixedDateTime.atZone(zone).toInstant(), zone);
    }
}
