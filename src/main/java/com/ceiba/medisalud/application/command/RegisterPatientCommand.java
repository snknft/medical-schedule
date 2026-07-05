package com.ceiba.medisalud.application.command;

import java.time.LocalDate;

/**
 * Carries the data required to register a patient from the application layer.
 */
public record RegisterPatientCommand(
        String fullName,
        String documentNumber,
        String phone,
        String email,
        LocalDate birthDate
) {
}
