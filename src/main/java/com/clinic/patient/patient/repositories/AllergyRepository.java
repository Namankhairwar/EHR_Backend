package com.clinic.patient.patient.repositories;

import com.clinic.patient.patient.entity.Allergy;
import com.clinic.patient.patient.state.AllergyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    List<Allergy> findAllByPatient_EhrId(String patientId);

    Optional<Allergy> findByallergyidAndPatient_EhrId(Long allergyid, String patientId);


    Optional<Allergy> findByallergyid(long id);

    boolean existsByPatient_EhrIdAndAllergyType(String patientEhrId, AllergyType allergyType);
}