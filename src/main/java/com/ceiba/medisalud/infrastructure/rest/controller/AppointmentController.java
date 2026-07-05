package com.ceiba.medisalud.infrastructure.rest.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ceiba.medisalud.application.command.RescheduleAppointmentCommand;
import com.ceiba.medisalud.application.command.ScheduleAppointmentCommand;
import com.ceiba.medisalud.application.usecase.AppointmentService;
import com.ceiba.medisalud.domain.exception.BadRequestException;
import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.domain.model.AppointmentStatus;
import com.ceiba.medisalud.infrastructure.rest.dto.AppointmentResponse;
import com.ceiba.medisalud.infrastructure.rest.dto.CreateAppointmentRequest;
import com.ceiba.medisalud.infrastructure.rest.dto.RescheduleAppointmentRequest;
import com.ceiba.medisalud.infrastructure.rest.mapper.ApiResponseMapper;

import jakarta.validation.Valid;

/**
 * Exposes REST endpoints for appointment scheduling, search, cancellation, rescheduling and attendance operations.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Creates the controller with the appointment application service.
     *
     * @param appointmentService application service that orchestrates appointment use cases
     */
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Creates a scheduled appointment from the request payload.
     *
     * @param request appointment creation payload containing patient, doctor and date-time data
     * @return created appointment response
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@Valid @RequestBody CreateAppointmentRequest request) {
        return ApiResponseMapper.toResponse(appointmentService.schedule(new ScheduleAppointmentCommand(
                request.patientId(),
                request.doctorId(),
                request.appointmentDateTime()
        )));
    }

    /**
     * Retrieves an appointment by identifier.
     *
     * @param id appointment identifier
     * @return appointment response
     */
    @GetMapping("/{id}")
    public AppointmentResponse getById(@PathVariable Long id) {
        return ApiResponseMapper.toResponse(appointmentService.getById(id));
    }

    /**
     * Searches appointments using optional filters. Both English and Spanish aliases are accepted for doctor and patient.
     *
     * @param doctorId optional doctor identifier filter
     * @param medicoId optional Spanish alias for {@code doctorId}
     * @param patientId optional patient identifier filter
     * @param pacienteId optional Spanish alias for {@code patientId}
     * @param status optional appointment status filter
     * @param fechaInicio optional initial date-time filter
     * @param fechaFin optional final date-time filter
     * @return appointments matching the provided filters
     */
    @GetMapping
    public List<AppointmentResponse> search(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ) {
        Long resolvedDoctorId = resolveAlias("doctorId", doctorId, "medicoId", medicoId);
        Long resolvedPatientId = resolveAlias("patientId", patientId, "pacienteId", pacienteId);
        return appointmentService.search(new AppointmentSearchCriteria(resolvedDoctorId, resolvedPatientId, status, fechaInicio, fechaFin))
                .stream()
                .map(ApiResponseMapper::toResponse)
                .toList();
    }

    /**
     * Cancels a scheduled appointment.
     *
     * @param id appointment identifier
     * @return cancelled appointment response
     */
    @PatchMapping("/{id}/cancel")
    public AppointmentResponse cancel(@PathVariable Long id) {
        return ApiResponseMapper.toResponse(appointmentService.cancel(id));
    }

    /**
     * Reschedules an existing appointment to a new date-time.
     *
     * @param id appointment identifier
     * @param request rescheduling payload containing the new appointment date-time
     * @return newly scheduled appointment created by the rescheduling operation
     */
    @PatchMapping("/{id}/reschedule")
    public AppointmentResponse reschedule(
            @PathVariable Long id,
            @Valid @RequestBody RescheduleAppointmentRequest request
    ) {
        return ApiResponseMapper.toResponse(appointmentService.reschedule(new RescheduleAppointmentCommand(id, request.newDateTime())));
    }

    /**
     * Marks an appointment as attended.
     *
     * @param id appointment identifier
     * @return attended appointment response
     */
    @PatchMapping("/{id}/attend")
    public AppointmentResponse attend(@PathVariable Long id) {
        return ApiResponseMapper.toResponse(appointmentService.attend(id));
    }

    /**
     * Resolves two equivalent query parameter names into a single identifier.
     *
     * @param primaryName canonical query parameter name
     * @param primaryValue canonical query parameter value
     * @param aliasName alternative query parameter name
     * @param aliasValue alternative query parameter value
     * @return resolved identifier or {@code null} when both values are absent
     */
    private Long resolveAlias(String primaryName, Long primaryValue, String aliasName, Long aliasValue) {
        if (primaryValue == null) {
            return aliasValue;
        }
        if (aliasValue == null || primaryValue.equals(aliasValue)) {
            return primaryValue;
        }
        throw new BadRequestException("Los parámetros " + primaryName + " y " + aliasName + " no pueden tener valores diferentes");
    }
}
