package com.clinic.patient.service.impl;

import com.clinic.patient.entities.Patient;
import com.clinic.patient.repositories.PatientRepository;
import com.clinic.patient.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicServiceImpl implements ClinicService {


    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<Patient> getPatient() {
        return patientRepository.findAll();
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails) {
        // Step 1: Find the existing patient by ID
        Optional<Patient> existingPatient = patientRepository.findById(id);

        // Step 2: If patient exists, update the fields
        if (existingPatient.isPresent()) {
            Patient patientToUpdate = existingPatient.get();

            // Step 3: Update the patient details with the new values
            // For example, update the name, address, and email
            patientToUpdate.setFirstName(patientDetails.getFirstName());
            patientToUpdate.setLastName(patientDetails.getLastName());
            patientToUpdate.setAge(patientDetails.getAge());
            patientToUpdate.setEmail(patientDetails.getEmail());
            patientToUpdate.setMobile(patientDetails.getMobile());
            patientToUpdate.setAddress(patientDetails.getAddress());
            patientToUpdate.setAllergy(patientDetails.getAllergy());
            // Add other fields you want to update here

            // Step 4: Save the updated patient back to the database
            return patientRepository.save(patientToUpdate);
        } else {
            // Patient not found, you can throw an exception or return a null value
            throw new RuntimeException("Patient not found with id: " + id);
        }
    }



    @Override
    public void deletePatient(Long id) {
        // Step 1: Check if the patient exists before attempting to delete
        if (patientRepository.existsById(id)) {
            // Step 2: Delete the patient by ID
            patientRepository.deleteById(id);
        } else {
            // Step 3: Handle the case where the patient doesn't exist (Optional)
            throw new RuntimeException("Patient not found with id: " + id);
        }
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }


}
