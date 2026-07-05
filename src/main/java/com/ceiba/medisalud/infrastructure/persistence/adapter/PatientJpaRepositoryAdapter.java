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
     * Creates a new PatientJpaRepositoryAdapter instance.
     */
    public PatientJpaRepositoryAdapter(SpringDataPatientJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * Persists the provided domain object and returns the saved instance.
     */
    @Override
    public Patient save(Patient patient) {
        return PatientJpaMapper.toDomain(repository.save(PatientJpaMapper.toEntity(patient)));
    }

    /**
     * Finds a resource by its identifier.
     */
    @Override
    public Optional<Patient> findById(Long id) {
        return repository.findById(id).map(PatientJpaMapper::toDomain);
    }

    /**
     * Returns all persisted resources of the current type.
     */
    @Override
    public List<Patient> findAll() {
        return repository.findAll().stream().map(PatientJpaMapper::toDomain).toList();
    }

    /**
     * Determines whether a patient already exists for the provided document number.
     */
    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.existsByDocumentNumber(documentNumber);
    }
}
