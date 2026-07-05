package com.ceiba.medisalud.infrastructure.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ceiba.medisalud.domain.policy.HolidayProvider;

@Component
public class ConfiguredHolidayProvider implements HolidayProvider {

    private final Set<LocalDate> holidays;

    public ConfiguredHolidayProvider(MedisaludProperties properties) {
        this.holidays = new HashSet<>(properties.getHolidays());
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }
}
