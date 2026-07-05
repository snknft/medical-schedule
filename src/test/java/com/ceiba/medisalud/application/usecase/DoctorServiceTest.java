package com.ceiba.medisalud.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceiba.medisalud.application.command.RegisterDoctorCommand;
import com.ceiba.medisalud.domain.exception.NotFoundException;
import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.domain.repository.DoctorRepositoryPort;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepositoryPort doctorRepository;

    private DoctorService service;

    @BeforeEach
    void setUp() {
        service = new DoctorService(doctorRepository);
    }

    @Test
    void registersDoctor() {
        RegisterDoctorCommand command = new RegisterDoctorCommand(
                "Dra. Laura Pérez",
                "Cardiología",
                "5552001",
                "laura@example.com"
        );
        Doctor savedDoctor = new Doctor(1L, command.fullName(), command.specialty(), command.phone(), command.email());
        when(doctorRepository.save(new Doctor(null, command.fullName(), command.specialty(), command.phone(), command.email())))
                .thenReturn(savedDoctor);

        Doctor result = service.register(command);

        assertThat(result).isEqualTo(savedDoctor);
    }

    @Test
    void getsDoctorById() {
        Doctor doctor = new Doctor(1L, "Dra. Laura Pérez", "Cardiología", "5552001", "laura@example.com");
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        assertThat(service.getById(1L)).isEqualTo(doctor);
    }

    @Test
    void throwsNotFoundWhenDoctorDoesNotExist() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Médico no encontrado");
    }

    @Test
    void listsDoctors() {
        List<Doctor> doctors = List.of(
                new Doctor(1L, "Dra. Laura Pérez", "Cardiología", "5552001", "laura@example.com"),
                new Doctor(2L, "Dr. Carlos Ruiz", "Pediatría", "5552002", "carlos@example.com")
        );
        when(doctorRepository.findAll()).thenReturn(doctors);

        assertThat(service.findAll()).containsExactlyElementsOf(doctors);
        verify(doctorRepository).findAll();
    }
}
