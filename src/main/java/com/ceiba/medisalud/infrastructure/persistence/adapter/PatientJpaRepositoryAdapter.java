package com.ceiba.medisalud.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ceiba.medisalud.domain.model.Patient;
import com.ceiba.medisalud.domain.repository.PatientRepositoryPort;
import com.ceiba.medisalud.infrastructure.persistence.mapper.PatientJpaMapper;
import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataPatientJpaRepository;

@Repository
public class PatientJpaRepositoryAdapter implements PatientRepositoryPort {

    private final SpringDataPatientJpaRepository repository;

    public PatientJpaRepositoryAdapter(SpringDataPatientJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Patient save(Patient patient) {
        return PatientJpaMapper.toDomain(repository.save(PatientJpaMapper.toEntity(patient)));
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return repository.findById(id).map(PatientJpaMapper::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return repository.findAll().stream().map(PatientJpaMapper::toDomain).toList();
    }

    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.existsByDocumentNumber(documentNumber);
    }
}
