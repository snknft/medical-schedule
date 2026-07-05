package com.ceiba.medisalud.infrastructure.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds MediSalud application properties from configuration files.
 */
@ConfigurationProperties(prefix = "medisalud")
public class MedisaludProperties {

    private List<LocalDate> holidays = new ArrayList<>();

    /**
     * Returns configured holidays.
     *
     * @return configured holiday dates
     */
    public List<LocalDate> getHolidays() {
        return holidays;
    }

    /**
     * Updates configured holidays.
     *
     * @param holidays holiday dates loaded from configuration
     */
    public void setHolidays(List<LocalDate> holidays) {
        this.holidays = holidays;
    }
}
