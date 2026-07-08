package com.clinic.patient.user.controller;

import com.clinic.patient.user.dto.PatientAllergiesDTO;
import com.clinic.patient.user.dto.PatientOverviewResponseDTO;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@AllArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{patientId}/overview")
    public ResponseEntity<PatientOverviewResponseDTO> getPatientOverview(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(
                patientService.getPatientOverview(patientId)
        );
    }

    @PutMapping("/{patientId}/emergency-contact")
    public ResponseEntity<PatientOverviewResponseDTO> updateEmergencyContact(
            @PathVariable Long patientId,
            @RequestBody User.Emergency emergencyContact
    ) {
        return ResponseEntity.ok(
                patientService.updateEmergencyContact(patientId, emergencyContact)
        );
    }

    @GetMapping("/{patientId}/allergies")
    public ResponseEntity<PatientAllergiesDTO> getPatientAllergies(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(
                patientService.getPatientAllergies(patientId)
        );
    }

    @PutMapping("/{patientId}/allergies")
    public ResponseEntity<PatientAllergiesDTO> updatePatientAllergies(
            @PathVariable Long patientId,
            @RequestBody PatientAllergiesDTO dto
    ) {
        return ResponseEntity.ok(
                patientService.updatePatientAllergies(patientId, dto)
        );
    }
}