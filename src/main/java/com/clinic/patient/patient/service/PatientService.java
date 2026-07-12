package com.clinic.patient.patient.service;

import com.clinic.patient.patient.dto.PatientOverviewResponseDTO;
import com.clinic.patient.user.entity.User;

public interface PatientService {

    PatientOverviewResponseDTO getPatientOverview(Long patientId);

    PatientOverviewResponseDTO updateEmergencyContact(
            Long patientId,
            User.Emergency emergencyContact
    );

}