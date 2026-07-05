package com.ceiba.medisalud.application.command;

public record RegisterDoctorCommand(
        String fullName,
        String specialty,
        String phone,
        String email
) {
}
