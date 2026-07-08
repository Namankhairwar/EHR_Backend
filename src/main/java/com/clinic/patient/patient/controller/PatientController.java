package com.clinic.patient.patient.controller;

import com.clinic.patient.patient.dto.PatientAllergiesDTO;
import com.clinic.patient.patient.dto.PatientOverviewResponseDTO;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.patient.repositories.PatientService;
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