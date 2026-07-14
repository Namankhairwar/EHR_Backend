package com.clinic.patient.patient.repositories;

import com.clinic.patient.patient.entity.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    List<Allergy> findAllByPatient_EhrId(Long patientId);

    Optional<Allergy> findByallergyidAndPatient_EhrId(Long allergyid, Long patientId);

    boolean existsByPatient_EhrIdAndAllergenNameIgnoreCase(
            Long patientId,
            String allergenName
    );
}