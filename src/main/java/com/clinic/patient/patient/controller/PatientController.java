package com.clinic.patient.patient.controller;

import com.clinic.patient.patient.dto.AllergyRequestDTO;
import com.clinic.patient.patient.dto.AllergyResponseDTO;
import com.clinic.patient.patient.dto.PatientOverviewResponseDTO;
import com.clinic.patient.patient.service.AllergyService;
import com.clinic.patient.patient.service.PatientService;
import com.clinic.patient.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AllergyService allergyService;

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
                patientService.updateEmergencyContact(
                        patientId,
                        emergencyContact
                )
        );
    }

    @PostMapping("/{patientId}/allergies")
    public ResponseEntity<AllergyResponseDTO> createAllergy(
            @PathVariable Long patientId,
            @RequestBody AllergyRequestDTO dto
    ) {

        AllergyResponseDTO response =
                allergyService.createAllergy(patientId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{patientId}/allergies")
    public ResponseEntity<List<AllergyResponseDTO>> getPatientAllergies(
            @PathVariable Long patientId
    ) {

        return ResponseEntity.ok(
                allergyService.getPatientAllergies(patientId)
        );
    }

    @DeleteMapping("/{patientId}/allergies/{allergyId}")
    public ResponseEntity<Void> deleteAllergy(
            @PathVariable Long patientId,
            @PathVariable Long allergyId
    ) {

        allergyService.deleteAllergy(patientId, allergyId);

        return ResponseEntity.noContent().build();
    }
}