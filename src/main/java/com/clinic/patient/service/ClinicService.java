package com.clinic.patient.service;

import com.clinic.patient.entities.Patient;

import java.util.List;
import java.util.Optional;

public interface ClinicService {

    List<Patient> getPatient();

    Patient savePatient(Patient patient);

    Patient updatePatient(Long id,Patient patient);

    void deletePatient(Long id);


    Optional<Patient> getPatientById(Long id);
}
