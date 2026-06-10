package com.clinic.patient.controller;


import com.clinic.patient.dto.AppointmentRequestDTO;
import com.clinic.patient.dto.AppointmentResponseDTO;
import com.clinic.patient.repositories.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Create Appointment
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentRequestDTO dto) {

        return ResponseEntity.ok(
                appointmentService.createAppointment(dto)
        );
    }

    // Get All Appointments
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    // Update Appointment
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentRequestDTO dto) {

        return ResponseEntity.ok(
                appointmentService.updateAppointment(id, dto)
        );
    }

    // Cancel Appointment
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.cancelAppointment(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
