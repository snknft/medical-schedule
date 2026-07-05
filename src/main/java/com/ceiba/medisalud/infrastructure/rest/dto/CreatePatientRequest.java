package com.ceiba.medisalud.infrastructure.rest.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents the REST request used to register a patient.
 */
public record CreatePatientRequest(
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre completo debe tener entre 3 y 100 caracteres")
        String fullName,

        @NotBlank(message = "El documento de identidad es obligatorio")
        @Size(min = 7, message = "El documento de identidad debe tener mínimo 7 caracteres")
        String documentNumber,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[0-9+\\-()\\s]{7,30}$", message = "El teléfono debe tener mínimo 7 dígitos o caracteres válidos")
        String phone,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato válido")
        String email,

        LocalDate birthDate
) {
}
