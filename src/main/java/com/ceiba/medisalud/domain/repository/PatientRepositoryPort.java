package com.ceiba.medisalud.domain.repository;

import java.util.List;
import java.util.Optional;

import com.ceiba.medisalud.domain.model.Patient;

public interface PatientRepositoryPort {

    Patient save(Patient patient);

    Optional<Patient> findById(Long id);

    List<Patient> findAll();

    boolean existsByDocumentNumber(String documentNumber);
}
