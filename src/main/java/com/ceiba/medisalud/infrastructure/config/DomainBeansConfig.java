package com.ceiba.medisalud.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ceiba.medisalud.domain.policy.DefaultMedicalWorkingHoursPolicy;
import com.ceiba.medisalud.domain.policy.HolidayProvider;
import com.ceiba.medisalud.domain.policy.WorkingHoursPolicy;
import com.ceiba.medisalud.domain.service.AppointmentFactory;
import com.ceiba.medisalud.domain.service.AppointmentRulesService;

@Configuration
public class DomainBeansConfig {

    @Bean
    public WorkingHoursPolicy workingHoursPolicy(HolidayProvider holidayProvider) {
        return new DefaultMedicalWorkingHoursPolicy(holidayProvider);
    }

    @Bean
    public AppointmentRulesService appointmentRulesService(WorkingHoursPolicy workingHoursPolicy) {
        return new AppointmentRulesService(workingHoursPolicy);
    }

    @Bean
    public AppointmentFactory appointmentFactory() {
        return new AppointmentFactory();
    }
}
