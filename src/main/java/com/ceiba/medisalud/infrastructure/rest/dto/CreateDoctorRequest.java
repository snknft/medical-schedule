package com.ceiba.medisalud.infrastructure.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateDoctorRequest(
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre completo debe tener entre 3 y 100 caracteres")
        String fullName,

        @NotBlank(message = "La especialidad es obligatoria")
        String specialty,

        @Pattern(regexp = "^[0-9+\\-()\\s]{7,30}$", message = "El teléfono debe tener mínimo 7 dígitos o caracteres válidos")
        String phone,

        @Email(message = "El email debe tener un formato válido")
        String email
) {
}
