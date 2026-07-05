package com.ceiba.medisalud.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ceiba.medisalud.domain.policy.DefaultMedicalWorkingHoursPolicy;
import com.ceiba.medisalud.domain.policy.HolidayProvider;
import com.ceiba.medisalud.domain.policy.WorkingHoursPolicy;
import com.ceiba.medisalud.domain.service.AppointmentFactory;
import com.ceiba.medisalud.domain.service.AppointmentRulesService;

/**
 * Creates domain services and policies as Spring beans without coupling the domain to Spring annotations.
 */
@Configuration
public class DomainBeansConfig {

    /**
     * Provides the working hours policy used by the domain layer.
     */
    @Bean
    public WorkingHoursPolicy workingHoursPolicy(HolidayProvider holidayProvider) {
        return new DefaultMedicalWorkingHoursPolicy(holidayProvider);
    }

    /**
     * Provides the appointment rules service used by application use cases.
     */
    @Bean
    public AppointmentRulesService appointmentRulesService(WorkingHoursPolicy workingHoursPolicy) {
        return new AppointmentRulesService(workingHoursPolicy);
    }

    /**
     * Provides the appointment factory used to create domain appointments.
     */
    @Bean
    public AppointmentFactory appointmentFactory() {
        return new AppointmentFactory();
    }
}
