package com.ceiba.medisalud.application.command;

/**
 * Carries the data required by the application layer to register a doctor.
 *
 * @param fullName doctor's complete name
 * @param specialty doctor's medical specialty
 * @param phone optional contact phone
 * @param email optional contact email
 */
public record RegisterDoctorCommand(
        String fullName,
        String specialty,
        String phone,
        String email
) {
}
