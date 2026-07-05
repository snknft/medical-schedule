package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDate;

/**
 * Represents the REST response returned for a patient.
 */
public record PatientResponse(
        Long id,
        String fullName,
        String documentNumber,
        String phone,
        String email,
        LocalDate birthDate
) {
}
