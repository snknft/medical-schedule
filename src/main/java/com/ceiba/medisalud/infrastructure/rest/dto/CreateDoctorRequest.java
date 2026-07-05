package com.ceiba.medisalud.infrastructure.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents the REST request used to register a doctor.
 *
 * @param fullName doctor's complete name, required between 3 and 100 characters
 * @param specialty medical specialty assigned to the doctor
 * @param phone optional contact phone; when present it must contain at least seven digits
 * @param email optional contact email with a valid format
 */
public record CreateDoctorRequest(
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre completo debe tener entre 3 y 100 caracteres")
        String fullName,

        @NotBlank(message = "La especialidad es obligatoria")
        String specialty,

        @Pattern(
                regexp = "^(?=(?:\\D*\\d){7,}\\D*$)[0-9+\\-()\\s]{7,30}$",
                message = "El teléfono debe contener mínimo 7 dígitos y solo caracteres válidos"
        )
        String phone,

        @Email(message = "El email debe tener un formato válido")
        String email
) {
}
