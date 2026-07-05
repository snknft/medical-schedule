package com.ceiba.medisalud.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.domain.repository.DoctorRepositoryPort;
import com.ceiba.medisalud.infrastructure.persistence.mapper.DoctorJpaMapper;
import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataDoctorJpaRepository;

/**
 * Implements a domain port using Spring Data JPA for doctor persistence.
 */
@Repository
public class DoctorJpaRepositoryAdapter implements DoctorRepositoryPort {

    private final SpringDataDoctorJpaRepository repository;

    /**
     * Creates the doctor adapter with the Spring Data repository.
     *
     * @param repository Spring Data repository used for doctor persistence
     */
    public DoctorJpaRepositoryAdapter(SpringDataDoctorJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * Persists the provided domain object and returns the saved instance.
     *
     * @param doctor domain doctor to persist
     * @return persisted doctor
     */
    @Override
    public Doctor save(Doctor doctor) {
        return DoctorJpaMapper.toDomain(repository.save(DoctorJpaMapper.toEntity(doctor)));
    }

    /**
     * Finds a resource by its identifier.
     *
     * @param id resource identifier
     * @return optional domain object
     */
    @Override
    public Optional<Doctor> findById(Long id) {
        return repository.findById(id).map(DoctorJpaMapper::toDomain);
    }

    /**
     * Returns all persisted resources of the current type.
     *
     * @return all persisted domain objects
     */
    @Override
    public List<Doctor> findAll() {
        return repository.findAll().stream().map(DoctorJpaMapper::toDomain).toList();
    }

    /**
     * Determines whether a resource exists for the provided identifier.
     *
     * @param id resource identifier
     * @return {@code true} when the resource exists
     */
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    /**
     * Counts the number of persisted resources.
     *
     * @return total number of persisted resources
     */
    @Override
    public long count() {
        return repository.count();
    }
}
