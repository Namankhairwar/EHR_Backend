package com.clinic.patient.user.repositories;

import com.clinic.patient.user.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends  JpaRepository<Patient, Long> {

     void deletePatientById(Long id);

    Optional<Patient> getPatientById(Long id);

}
