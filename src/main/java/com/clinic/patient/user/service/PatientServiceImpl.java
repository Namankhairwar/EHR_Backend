package com.clinic.patient.user.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.user.dto.PatientAllergiesDTO;
import com.clinic.patient.user.dto.PatientOverviewResponseDTO;
import com.clinic.patient.user.entity.Patient;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.PatientRepository;
import com.clinic.patient.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    public PatientOverviewResponseDTO getPatientOverview(Long patientId) {

        Patient patient = getPatient(patientId);
        User user = getLinkedUser(patient);

        return PatientOverviewResponseDTO.builder()
                .emergencyContact(user.getEmergencyContact())
                .bloodGroup(user.getBloodGroup())
                .allergies(
                        patient.getAllergies() == null
                                ? new ArrayList<>()
                                : patient.getAllergies()
                )
                .build();
    }

    @Override
    public PatientOverviewResponseDTO updateEmergencyContact(
            Long patientId,
            User.Emergency emergencyContact
    ) {

        Patient patient = getPatient(patientId);
        User user = getLinkedUser(patient);

        user.setEmergencyContact(emergencyContact);
        userRepository.save(user);

        return getPatientOverview(patientId);
    }

    @Override
    public PatientAllergiesDTO getPatientAllergies(Long patientId) {

        Patient patient = getPatient(patientId);

        return PatientAllergiesDTO.builder()
                .allergies(
                        patient.getAllergies() == null
                                ? new ArrayList<>()
                                : patient.getAllergies()
                )
                .build();
    }

    @Override
    public PatientAllergiesDTO updatePatientAllergies(
            Long patientId,
            PatientAllergiesDTO dto
    ) {

        Patient patient = getPatient(patientId);

        patient.setAllergies(dto.getAllergies());
        patientRepository.save(patient);

        return MAP.map(dto, PatientAllergiesDTO::new);
    }

    private Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    private User getLinkedUser(Patient patient) {
        return userRepository.findByEmail(patient.getEmail())
                .orElseThrow(() -> new RuntimeException("Linked user not found"));
    }
}