package com.ceiba.medisalud.infrastructure.rest.dto;

/**
 * Represents the REST response returned for a doctor.
 */
public record DoctorResponse(
        Long id,
        String fullName,
        String specialty,
        String phone,
        String email
) {
}
