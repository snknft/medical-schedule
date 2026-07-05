package com.ceiba.medisalud.domain.model;

/**
 * Represents a doctor in the domain model.
 *
 * @param id doctor identifier
 * @param fullName doctor's complete name
 * @param specialty doctor's medical specialty
 * @param phone optional contact phone
 * @param email optional contact email
 */
public record Doctor(
        Long id,
        String fullName,
        String specialty,
        String phone,
        String email
) {
}
