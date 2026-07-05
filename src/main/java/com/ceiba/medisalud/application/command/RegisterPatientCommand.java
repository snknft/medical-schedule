package com.ceiba.medisalud.application.command;

import java.time.LocalDate;

/**
 * Carries the data required by the application layer to register a patient.
 *
 * @param fullName patient's complete name
 * @param documentNumber unique patient identity document
 * @param phone required contact phone
 * @param email required contact email
 * @param birthDate optional patient birth date
 */
public record RegisterPatientCommand(
        String fullName,
        String documentNumber,
        String phone,
        String email,
        LocalDate birthDate
) {
}
