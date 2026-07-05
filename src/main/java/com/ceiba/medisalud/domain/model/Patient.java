package com.ceiba.medisalud.domain.model;

import java.time.LocalDate;

/**
 * Represents a patient in the domain model.
 *
 * @param id patient identifier
 * @param fullName patient's complete name
 * @param documentNumber unique identity document
 * @param phone required contact phone
 * @param email required contact email
 * @param birthDate optional birth date used by scheduling rules
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
