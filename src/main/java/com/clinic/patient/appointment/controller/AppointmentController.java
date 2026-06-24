package com.clinic.patient.appointment.controller;


import com.clinic.patient.appointment.dto.AppointmentRequestDTO;
import com.clinic.patient.appointment.dto.AppointmentResponseDTO;
import com.clinic.patient.appointment.service.AppointmentService;
import com.clinic.patient.appointment.state.AppointmentStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentRequestDTO dto) {

        return ResponseEntity.ok(
                appointmentService.createAppointment(dto)
        );
    }

    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(appointmentId)
        );
    }

    @PatchMapping("/appointments/{appointmentId}/reschedule")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentRequestDTO dto) {

        return ResponseEntity.ok(
                appointmentService.rescheduleAppointment(
                        appointmentId,
                        dto
                )
        );
    }

    @PatchMapping("/appointments/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentRequestDTO dto) {

        return ResponseEntity.ok(
                appointmentService.cancelAppointment(
                        appointmentId,
                        dto
                )
        );
    }

    @GetMapping("/patients/{patientId}/appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatient(
            @PathVariable Long patientId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) Boolean upcoming) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatient(
                        patientId,
                        status,
                        upcoming
                )
        );
    }

    @GetMapping("/doctors/{doctorId}/appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) LocalDate date) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDoctor(
                        doctorId,
                        status,
                        date
                )
        );
    }
}