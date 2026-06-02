package com.clinic.patient.service;

import com.clinic.patient.entities.Patient;
import com.clinic.patient.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicServiceImpl {

    private final PatientRepository patientRepository;

    @Autowired
    public ClinicServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    public List<Patient> getPatient() {
        return patientRepository.findAll();
    }


    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Optional<Patient> existingPatient = patientRepository.findById(id);
        if (existingPatient.isPresent()) {
            return patientRepository.save(patientDetails);
        } else {
            throw new RuntimeException("Patient not found with id: " + id);
        }
    }

    public void deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        } else {
            throw new RuntimeException("Patient not found with id: " + id);
        }
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }


}
