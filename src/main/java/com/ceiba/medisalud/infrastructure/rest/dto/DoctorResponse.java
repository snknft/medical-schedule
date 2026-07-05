package com.ceiba.medisalud.infrastructure.rest.dto;

public record DoctorResponse(
        Long id,
        String fullName,
        String specialty,
        String phone,
        String email
) {
}
