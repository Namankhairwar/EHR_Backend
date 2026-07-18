package com.clinic.patient.patient.controller;

import com.clinic.patient.patient.dto.AllergyRequestDTO;
import com.clinic.patient.patient.dto.AllergyResponseDTO;
import com.clinic.patient.patient.entity.Allergy;
import com.clinic.patient.patient.repositories.AllergyRepository;
import com.clinic.patient.patient.service.AllergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {


    private final AllergyService allergyService;
    private final AllergyRepository allergyRepository;


    @PostMapping("/{patientId}/allergies")
    public ResponseEntity<AllergyResponseDTO> createAllergy(
            @PathVariable String patientId,
            @RequestBody AllergyRequestDTO dto
    ) {
       allergyService.createAllergy(patientId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    @PutMapping("/{patientId}/allergies")
    public ResponseEntity<Void> updateAllergy(
            @PathVariable String patientId,
            @RequestBody AllergyRequestDTO dto
    ) {

       boolean alreadyExists =
                allergyService.check(patientId,dto);
        if (!alreadyExists) {
            throw new RuntimeException(
                    "This allergy is not registered for the patient"
            );
        }

        Allergy allergy = allergyRepository.findAllByPatient_EhrId(patientId).stream()
                .filter(a -> a.getAllergyType() == dto.getAllergyType())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Allergy not found"));

        com.clinic.patient.applicationCommonFeature.mapping.MAP.copyInTheObject(dto, allergy);
        allergyRepository.save(allergy);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }




    @GetMapping("/{patientId}/allergies")
    public ResponseEntity<List<AllergyResponseDTO>> getPatientAllergies(
            @PathVariable String patientId
    ) {

        return ResponseEntity.ok(
                allergyService.getPatientAllergies(patientId)
        );
    }

    @DeleteMapping("/{patientId}/allergies/{allergyId}")
    public ResponseEntity<Void> deleteAllergy(
            @PathVariable String patientId,
            @PathVariable Long allergyId
    ) {

        allergyService.deleteAllergy(patientId, allergyId);

        return ResponseEntity.noContent().build();
    }
}