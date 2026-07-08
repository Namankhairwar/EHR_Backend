package com.clinic.patient.patient.repositories;

import com.clinic.patient.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends  JpaRepository<Patient, Long> {

      void deletePatientById(Long id);

    Optional<Patient> getPatientById(Long id);

}
