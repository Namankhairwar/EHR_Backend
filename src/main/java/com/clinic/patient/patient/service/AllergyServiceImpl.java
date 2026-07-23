package com.clinic.patient.patient.service;

import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.patient.dto.AllergyRequestDTO;
import com.clinic.patient.patient.dto.AllergyResponseDTO;
import com.clinic.patient.patient.entity.Allergy;
import com.clinic.patient.patient.repositories.AllergyRepository;
import com.clinic.patient.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;

    @Override
    @Transactional
    public AllergyResponseDTO createAllergy(
            String patientId,
            AllergyRequestDTO dto
    ) {



        boolean alreadyExists =check(patientId,dto);


        if (alreadyExists) {
            throw new RuntimeException(
                    "This allergy is already registered for the patient"
            );
        }

        Allergy allergy = MAP.map(dto, Allergy::new);
        User user  =new User();
        user.setEhrId(patientId);
        allergy.setPatient(user);
        Allergy savedAllergy = allergyRepository.save(allergy);

        return mapToResponse(savedAllergy);
    }

    @Override
    public boolean check(String patientId , AllergyRequestDTO dto){
      return  allergyRepository
                .existsByPatient_EhrIdAndAllergyType(patientId , dto.getAllergyType());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllergyResponseDTO> getPatientAllergies(String patientId) {

        return allergyRepository.findAllByPatient_EhrId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAllergy(
            String patientId,
            Long allergyId
    ) {

        Allergy allergy = allergyRepository
                .findByallergyidAndPatient_EhrId(allergyId, patientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Allergy not found for patient"
                        )
                );

        allergyRepository.delete(allergy);
    }


    private AllergyResponseDTO mapToResponse(Allergy allergy) {

        return MAP.map(
                allergy,
                AllergyResponseDTO::new
        );
    }
}