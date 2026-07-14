package com.clinic.patient.patient.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.patient.dto.AllergyRequestDTO;
import com.clinic.patient.patient.dto.AllergyResponseDTO;
import com.clinic.patient.patient.entity.Allergy;
import com.clinic.patient.patient.entity.Patient;
import com.clinic.patient.patient.repositories.AllergyRepository;
import com.clinic.patient.patient.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public AllergyResponseDTO createAllergy(
            Long patientId,
            AllergyRequestDTO dto
    ) {

        Patient patient = getPatient(patientId);

        boolean alreadyExists =
                allergyRepository
                        .existsByPatient_IdAndAllergenNameIgnoreCase(
                                patientId,
                                dto.getAllergenName()
                        );

        if (alreadyExists) {
            throw new RuntimeException(
                    "This allergy is already registered for the patient"
            );
        }

        Allergy allergy = MAP.map(dto, Allergy::new);
        allergy.setPatient(patient);

        Allergy savedAllergy = allergyRepository.save(allergy);

        return mapToResponse(savedAllergy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllergyResponseDTO> getPatientAllergies(Long patientId) {

        getPatient(patientId);

        return allergyRepository.findAllByPatient_Id(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAllergy(
            Long patientId,
            Long allergyId
    ) {

        Allergy allergy = allergyRepository
                .findByIdAndPatient_Id(allergyId, patientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Allergy not found for patient"
                        )
                );

        allergyRepository.delete(allergy);
    }

    private Patient getPatient(Long patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: " + patientId
                        )
                );
    }

    private AllergyResponseDTO mapToResponse(Allergy allergy) {

        return MAP.map(
                allergy,
                AllergyResponseDTO::new,
                "patient",
                "allergyType"
        );
    }
}