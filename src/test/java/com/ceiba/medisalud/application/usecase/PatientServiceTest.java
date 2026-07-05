package com.ceiba.medisalud.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceiba.medisalud.application.command.RegisterPatientCommand;
import com.ceiba.medisalud.domain.exception.ConflictException;
import com.ceiba.medisalud.domain.exception.NotFoundException;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.repository.PatientRepositoryPort;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepositoryPort patientRepository;

    private PatientService service;

    @BeforeEach
    void setUp() {
        service = new PatientService(patientRepository);
    }

    @Test
    void registersPatientWhenDocumentIsUnique() {
        LocalDate birthDate = LocalDate.of(1995, 5, 20);
        RegisterPatientCommand command = new RegisterPatientCommand(
                "Juan Pérez",
                "123456789",
                "3001234567",
                "juan@example.com",
                birthDate
        );
        Patient savedPatient = new Patient(1L, command.fullName(), command.documentNumber(), command.phone(), command.email(), birthDate);
        when(patientRepository.existsByDocumentNumber(command.documentNumber())).thenReturn(false);
        when(patientRepository.save(new Patient(null, command.fullName(), command.documentNumber(), command.phone(), command.email(), birthDate)))
                .thenReturn(savedPatient);

        Patient result = service.register(command);

        assertThat(result).isEqualTo(savedPatient);
    }

    @Test
    void rejectsDuplicatedDocumentNumber() {
        RegisterPatientCommand command = new RegisterPatientCommand(
                "Juan Pérez",
                "123456789",
                "3001234567",
                "juan@example.com",
                LocalDate.of(1995, 5, 20)
        );
        when(patientRepository.existsByDocumentNumber(command.documentNumber())).thenReturn(true);

        assertThatThrownBy(() -> service.register(command))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Ya existe un paciente");

        verify(patientRepository, never()).save(org.mockito.ArgumentMatchers.any(Patient.class));
    }

    @Test
    void getsPatientById() {
        Patient patient = new Patient(1L, "Juan Pérez", "123456789", "3001234567", "juan@example.com", LocalDate.of(1995, 5, 20));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        assertThat(service.getById(1L)).isEqualTo(patient);
    }

    @Test
    void throwsNotFoundWhenPatientDoesNotExist() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Paciente no encontrado");
    }

    @Test
    void listsPatients() {
        List<Patient> patients = List.of(
                new Patient(1L, "Juan Pérez", "123456789", "3001234567", "juan@example.com", LocalDate.of(1995, 5, 20)),
                new Patient(2L, "Ana Torres", "987654321", "3007654321", "ana@example.com", null)
        );
        when(patientRepository.findAll()).thenReturn(patients);

        assertThat(service.findAll()).containsExactlyElementsOf(patients);
        verify(patientRepository).findAll();
    }
}
