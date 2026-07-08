package com.clinic.patient.patient.repositories;

import com.clinic.patient.patient.dto.PatientAllergiesDTO;
import com.clinic.patient.patient.dto.PatientOverviewResponseDTO;
import com.clinic.patient.user.entity.User;

public interface PatientService {

    PatientOverviewResponseDTO getPatientOverview(Long patientId);

    PatientOverviewResponseDTO updateEmergencyContact(
            Long patientId,
            User.Emergency emergencyContact
    );

    PatientAllergiesDTO getPatientAllergies(Long patientId);

    PatientAllergiesDTO updatePatientAllergies(
            Long patientId,
            PatientAllergiesDTO dto
    );
}