package com.ceiba.medisalud.infrastructure.rest.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ceiba.medisalud.application.command.RegisterDoctorCommand;
import com.ceiba.medisalud.application.query.AvailableSlotsQuery;
import com.ceiba.medisalud.application.usecase.AppointmentService;
import com.ceiba.medisalud.application.usecase.DoctorService;
import com.ceiba.medisalud.infrastructure.rest.dto.AvailableSlotResponse;
import com.ceiba.medisalud.infrastructure.rest.dto.CreateDoctorRequest;
import com.ceiba.medisalud.infrastructure.rest.dto.DoctorResponse;
import com.ceiba.medisalud.infrastructure.rest.mapper.ApiResponseMapper;

import jakarta.validation.Valid;

/**
 * Exposes REST endpoints for doctor operations.
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    /**
     * Creates a new DoctorController instance.
     */
    public DoctorController(DoctorService doctorService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    /**
     * Handles the REST request that creates a new resource.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponse create(@Valid @RequestBody CreateDoctorRequest request) {
        return ApiResponseMapper.toResponse(doctorService.register(new RegisterDoctorCommand(
                request.fullName(),
                request.specialty(),
                request.phone(),
                request.email()
        )));
    }

    /**
     * Returns all persisted resources of the current type.
     */
    @GetMapping
    public List<DoctorResponse> findAll() {
        return doctorService.findAll().stream().map(ApiResponseMapper::toResponse).toList();
    }

    /**
     * Handles the REST request that retrieves a doctor by identifier.
     */
    @GetMapping("/{id}")
    public DoctorResponse getById(@PathVariable Long id) {
        return ApiResponseMapper.toResponse(doctorService.getById(id));
    }

    /**
     * Handles the REST request that returns available slots for a doctor.
     */
    @GetMapping("/{doctorId}/available-slots")
    public List<AvailableSlotResponse> availableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        return appointmentService.findAvailableSlots(new AvailableSlotsQuery(doctorId, fechaInicio, fechaFin))
                .stream()
                .map(ApiResponseMapper::toResponse)
                .toList();
    }
}
