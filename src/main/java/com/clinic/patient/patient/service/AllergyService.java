package com.clinic.patient.patient.service;

import com.clinic.patient.patient.dto.AllergyRequestDTO;
import com.clinic.patient.patient.dto.AllergyResponseDTO;

import java.util.List;

public interface AllergyService {

    AllergyResponseDTO createAllergy(
            String patientId,
            AllergyRequestDTO dto
    );

    List<AllergyResponseDTO> getPatientAllergies(String patientId);

    void deleteAllergy(
            String patientId,
            Long allergyId
    );

   boolean check(String patientId , AllergyRequestDTO dto);
}