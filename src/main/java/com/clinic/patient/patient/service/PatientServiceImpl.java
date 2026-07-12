package com.clinic.patient.patient.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.patient.dto.AllergyResponseDTO;
import com.clinic.patient.patient.dto.PatientOverviewResponseDTO;
import com.clinic.patient.patient.entity.Patient;
import com.clinic.patient.patient.repositories.AllergyRepository;
import com.clinic.patient.patient.repositories.PatientRepository;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AllergyRepository allergyRepository;

    @Override
    @Transactional(readOnly = true)
    public PatientOverviewResponseDTO getPatientOverview(Long patientId) {

        Patient patient = getPatient(patientId);
        User user = getLinkedUser(patient);

        List<AllergyResponseDTO> allergies =
                allergyRepository.findAllByPatient_Id(patientId)
                        .stream()
                        .map(allergy ->
                                MAP.map(
                                        allergy,
                                        AllergyResponseDTO::new,
                                        "patient",
                                        "allergyType"
                                )
                        )
                        .toList();

        return PatientOverviewResponseDTO.builder()
                .ehrid(String.valueOf(user.getEhrId()))
                .bloodGroup(user.getBloodGroup())
                .allergies(allergies)
                .emergencyContact(user.getEmergencyContact())
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

    public Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found")
                );
    }

    private User getLinkedUser(Patient patient) {
        return userRepository.findByEmail(patient.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Linked user not found")
                );
    }
}