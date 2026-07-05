package com.ceiba.medisalud.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ceiba.medisalud.domain.model.Doctor;
import com.ceiba.medisalud.domain.repository.DoctorRepositoryPort;
import com.ceiba.medisalud.infrastructure.persistence.mapper.DoctorJpaMapper;
import com.ceiba.medisalud.infrastructure.persistence.springdata.SpringDataDoctorJpaRepository;

@Repository
public class DoctorJpaRepositoryAdapter implements DoctorRepositoryPort {

    private final SpringDataDoctorJpaRepository repository;

    public DoctorJpaRepositoryAdapter(SpringDataDoctorJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Doctor save(Doctor doctor) {
        return DoctorJpaMapper.toDomain(repository.save(DoctorJpaMapper.toEntity(doctor)));
    }

    @Override
    public Optional<Doctor> findById(Long id) {
        return repository.findById(id).map(DoctorJpaMapper::toDomain);
    }

    @Override
    public List<Doctor> findAll() {
        return repository.findAll().stream().map(DoctorJpaMapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
