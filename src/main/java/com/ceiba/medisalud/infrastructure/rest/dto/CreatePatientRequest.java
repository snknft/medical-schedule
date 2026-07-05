package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents the REST request used to register a patient.
 *
 * @param fullName patient's complete name, required between 3 and 100 characters
 * @param documentNumber unique identity document, required with at least seven characters
 * @param phone required contact phone containing at least seven digits
 * @param email required contact email with a valid format
 * @param birthDate optional birth date validated when scheduling appointments
 */
public record CreatePatientRequest(
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre completo debe tener entre 3 y 100 caracteres")
        String fullName,

        @NotBlank(message = "El documento de identidad es obligatorio")
        @Size(min = 7, message = "El documento de identidad debe tener mínimo 7 caracteres")
        String documentNumber,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(
                regexp = "^(?=(?:\\D*\\d){7,}\\D*$)[0-9+\\-()\\s]{7,30}$",
                message = "El teléfono debe contener mínimo 7 dígitos y solo caracteres válidos"
        )
        String phone,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato válido")
        String email,

        LocalDate birthDate
) {
}
