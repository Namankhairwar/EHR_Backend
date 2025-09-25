package com.clinic.patient.repositories;

import com.clinic.patient.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends  JpaRepository<Patient, Long> {

    Optional<Patient> findByFirstName(String firstName);

    Optional<Patient> findByAllergy(String allergy);

}
