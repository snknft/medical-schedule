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
     * Provides the application clock used by time-sensitive rules.
     *
     * <p>By default, the application uses the system clock. For local demonstrations
     * and deterministic validation of time-sensitive flows, the clock can be fixed
     * by setting the {@code medisalud.clock.fixed-now} property with an ISO-8601
     * local date-time value, for example {@code 2026-07-06T07:30:00}.</p>
     *
     * @param fixedNow optional ISO-8601 local date-time used to fix the application clock
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
