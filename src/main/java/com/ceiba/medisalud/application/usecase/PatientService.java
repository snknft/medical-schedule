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
     * Creates the patient service with the patient persistence port.
     *
     * @param patientRepository port used to persist and query patients
     */
    public PatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Registers a new patient after checking document uniqueness.
     *
     * @param command registration command with patient data
     * @return persisted patient
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
     * Retrieves a patient by identifier.
     *
     * @param id patient identifier
     * @return patient when found
     */
    @Transactional(readOnly = true)
    public Patient getById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con id " + id));
    }

    /**
     * Lists all registered patients.
     *
     * @return patients currently persisted in the system
     */
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }
}
