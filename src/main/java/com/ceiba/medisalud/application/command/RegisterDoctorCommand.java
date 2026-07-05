package com.ceiba.medisalud.application.command;

/**
 * Carries the data required to register a doctor from the application layer.
 */
public record RegisterDoctorCommand(
        String fullName,
        String specialty,
        String phone,
        String email
) {
}
