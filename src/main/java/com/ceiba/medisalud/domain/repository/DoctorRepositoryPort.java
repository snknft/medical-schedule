package com.ceiba.medisalud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.ceiba.medisalud.domain.model.Doctor;

public interface DoctorRepositoryPort {

    Doctor save(Doctor doctor);

    Optional<Doctor> findById(Long id);

    List<Doctor> findAll();

    boolean existsById(Long id);

    long count();
}
