package com.clinic.patient.patient.service;

import com.clinic.patient.patient.dto.AllergyRequestDTO;
import com.clinic.patient.patient.dto.AllergyResponseDTO;

import java.util.List;

public interface AllergyService {

    AllergyResponseDTO createAllergy(
            Long patientId,
            AllergyRequestDTO dto
    );

    List<AllergyResponseDTO> getPatientAllergies(Long patientId);

    void deleteAllergy(
            Long patientId,
            Long allergyId
    );
}