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
     * Returns the holidays value.
     */
    public List<LocalDate> getHolidays() {
        return holidays;
    }

    /**
     * Updates the holidays value.
     */
    public void setHolidays(List<LocalDate> holidays) {
        this.holidays = holidays;
    }
}
