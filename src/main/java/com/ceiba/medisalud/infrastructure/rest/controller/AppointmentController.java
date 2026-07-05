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
import com.ceiba.medisalud.domain.model.AppointmentSearchCriteria;
import com.ceiba.medisalud.application.usecase.AppointmentService;
import com.ceiba.medisalud.domain.model.AppointmentStatus;
import com.ceiba.medisalud.infrastructure.rest.dto.AppointmentResponse;
import com.ceiba.medisalud.infrastructure.rest.dto.CreateAppointmentRequest;
import com.ceiba.medisalud.infrastructure.rest.dto.RescheduleAppointmentRequest;
import com.ceiba.medisalud.infrastructure.rest.mapper.ApiResponseMapper;

import jakarta.validation.Valid;

/**
 * Exposes REST endpoints for appointment operations.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Creates a new AppointmentController instance.
     */
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Handles the REST request that creates a new resource.
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
     * Handles the REST request that retrieves an appointment by identifier.
     */
    @GetMapping("/{id}")
    public AppointmentResponse getById(@PathVariable Long id) {
        return ApiResponseMapper.toResponse(appointmentService.getById(id));
    }

    /**
     * Searches appointments using the provided optional filters.
     */
    @GetMapping
    public List<AppointmentResponse> search(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ) {
        return appointmentService.search(new AppointmentSearchCriteria(doctorId, patientId, status, fechaInicio, fechaFin))
                .stream()
                .map(ApiResponseMapper::toResponse)
                .toList();
    }

    /**
     * Handles the REST request that cancels an appointment.
     */
    @PatchMapping("/{id}/cancel")
    public AppointmentResponse cancel(@PathVariable Long id) {
        return ApiResponseMapper.toResponse(appointmentService.cancel(id));
    }

    /**
     * Handles the REST request that reschedules an appointment.
     */
    @PatchMapping("/{id}/reschedule")
    public AppointmentResponse reschedule(
            @PathVariable Long id,
            @Valid @RequestBody RescheduleAppointmentRequest request
    ) {
        return ApiResponseMapper.toResponse(appointmentService.reschedule(new RescheduleAppointmentCommand(id, request.newDateTime())));
    }

    /**
     * Handles the REST request that marks an appointment as attended.
     */
    @PatchMapping("/{id}/attend")
    public AppointmentResponse attend(@PathVariable Long id) {
        return ApiResponseMapper.toResponse(appointmentService.attend(id));
    }
}
