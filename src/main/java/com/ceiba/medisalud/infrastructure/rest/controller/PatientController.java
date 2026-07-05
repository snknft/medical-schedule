package com.ceiba.medisalud.infrastructure.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ceiba.medisalud.application.command.RegisterPatientCommand;
import com.ceiba.medisalud.application.usecase.PatientService;
import com.ceiba.medisalud.infrastructure.rest.dto.CreatePatientRequest;
import com.ceiba.medisalud.infrastructure.rest.dto.PatientResponse;
import com.ceiba.medisalud.infrastructure.rest.mapper.ApiResponseMapper;

import jakarta.validation.Valid;

/**
 * Exposes REST endpoints for patient operations.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    /**
     * Creates a new PatientController instance.
     */
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Handles the REST request that creates a new resource.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse create(@Valid @RequestBody CreatePatientRequest request) {
        return ApiResponseMapper.toResponse(patientService.register(new RegisterPatientCommand(
                request.fullName(),
                request.documentNumber(),
                request.phone(),
                request.email(),
                request.birthDate()
        )));
    }

    /**
     * Returns all persisted resources of the current type.
     */
    @GetMapping
    public List<PatientResponse> findAll() {
        return patientService.findAll().stream().map(ApiResponseMapper::toResponse).toList();
    }

    /**
     * Handles the REST request that retrieves a patient by identifier.
     */
    @GetMapping("/{id}")
    public PatientResponse getById(@PathVariable Long id) {
        return ApiResponseMapper.toResponse(patientService.getById(id));
    }
}
