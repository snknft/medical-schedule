package com.ceiba.medisalud.domain.model;

public record Doctor(
        Long id,
        String fullName,
        String specialty,
        String phone,
        String email
) {
}
