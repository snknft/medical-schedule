package com.ceiba.medisalud.domain.model;

import java.time.LocalDateTime;


public record AppointmentSearchCriteria(
        Long doctorId,
        Long patientId,
        AppointmentStatus status,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin
) {
}
