package com.ceiba.medisalud.infrastructure.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ceiba.medisalud.domain.policy.HolidayProvider;

/**
 * Provides holiday lookup based on configured application properties.
 */
@Component
public class ConfiguredHolidayProvider implements HolidayProvider {

    private final Set<LocalDate> holidays;

    /**
     * Creates the provider from configured MediSalud properties.
     *
     * @param properties application properties that contain configured holidays
     */
    public ConfiguredHolidayProvider(MedisaludProperties properties) {
        this.holidays = new HashSet<>(properties.getHolidays());
    }

    /**
     * Determines whether the provided date is configured as a holiday.
     *
     * @param date date to evaluate
     * @return {@code true} when the date is configured as a holiday
     */
    @Override
    public boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }
}
