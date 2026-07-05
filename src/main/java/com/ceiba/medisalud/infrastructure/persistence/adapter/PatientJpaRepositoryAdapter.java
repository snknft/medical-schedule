package com.ceiba.medisalud.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.repository.PatientRepositoryPort;
import com.ceiba.medisalud.infrastructure.persistence.mapper.PatientJpaMapper;
import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataPatientJpaRepository;

/**
 * Implements a domain port using Spring Data JPA for patient persistence.
 */
@Repository
public class PatientJpaRepositoryAdapter implements PatientRepositoryPort {

    private final SpringDataPatientJpaRepository repository;

    /**
     * Creates the patient adapter with the Spring Data repository.
     *
     * @param repository Spring Data repository used for patient persistence
     */
    public PatientJpaRepositoryAdapter(SpringDataPatientJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * Persists the provided domain object and returns the saved instance.
     *
     * @param patient domain patient to persist
     * @return persisted patient
     */
    @Override
    public Patient save(Patient patient) {
        return PatientJpaMapper.toDomain(repository.save(PatientJpaMapper.toEntity(patient)));
    }

    /**
     * Finds a resource by its identifier.
     *
     * @param id resource identifier
     * @return optional domain object
     */
    @Override
    public Optional<Patient> findById(Long id) {
        return repository.findById(id).map(PatientJpaMapper::toDomain);
    }

    /**
     * Returns all persisted resources of the current type.
     *
     * @return all persisted domain objects
     */
    @Override
    public List<Patient> findAll() {
        return repository.findAll().stream().map(PatientJpaMapper::toDomain).toList();
    }

    /**
     * Determines whether a patient already exists for the provided document number.
     *
     * @param documentNumber patient identity document number
     * @return {@code true} when the document number is already registered
     */
    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.existsByDocumentNumber(documentNumber);
    }
}
