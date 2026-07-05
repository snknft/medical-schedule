package com.ceiba.medisalud.domain.model;

import java.time.LocalDate;

/**
 * Represents a patient in the domain model.
 */
public record Patient(
        Long id,
        String fullName,
        String documentNumber,
        String phone,
        String email,
        LocalDate birthDate
) {
}
