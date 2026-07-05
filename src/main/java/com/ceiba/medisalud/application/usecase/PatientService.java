package com.ceiba.medisalud.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceiba.medisalud.application.command.RegisterPatientCommand;
import com.ceiba.medisalud.domain.exception.ConflictException;
import com.ceiba.medisalud.domain.exception.NotFoundException;
import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.repository.PatientRepositoryPort;

/**
 * Coordinates patient registration and query use cases.
 */
@Service
@Transactional
public class PatientService {

    private final PatientRepositoryPort patientRepository;

    /**
     * Creates a new PatientService instance.
     */
    public PatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Registers a new domain resource after validating its business constraints.
     */
    public Patient register(RegisterPatientCommand command) {
        if (patientRepository.existsByDocumentNumber(command.documentNumber())) {
            throw new ConflictException("Ya existe un paciente con el documento " + command.documentNumber());
        }
        Patient patient = new Patient(
                null,
                command.fullName(),
                command.documentNumber(),
                command.phone(),
                command.email(),
                command.birthDate()
        );
        return patientRepository.save(patient);
    }

    /**
     * Returns the byId value.
     */
    @Transactional(readOnly = true)
    public Patient getById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con id " + id));
    }

    /**
     * Returns all persisted resources of the current type.
     */
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }
}
