package com.ceiba.medisalud.application.command;

import java.time.LocalDate;

public record RegisterPatientCommand(
        String fullName,
        String documentNumber,
        String phone,
        String email,
        LocalDate birthDate
) {
}
