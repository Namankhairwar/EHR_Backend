package com.clinic.patient.user.controller;

import com.clinic.patient.user.entity.Patient;
import com.clinic.patient.user.service.ClinicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/CMS")
public class ClinicController {

    @Autowired
    private ClinicServiceImpl clinicService;

    @GetMapping("/getPatient")
    public List<Patient> getPatient() {
        return clinicService.getPatient();
    }


    // GET: Retrieve a specific patient by ID  localhost:8080/CMS/123456
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = clinicService.getPatientById(id);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    // POST: Create a new patient
    @PostMapping("/create")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdPatient = clinicService.savePatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);  // 201 Created
    }

    // PUT: Update an existing patient by ID
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails) {
        try {
            Patient updatedPatient = clinicService.updatePatient(id, patientDetails);
            return ResponseEntity.ok(updatedPatient);  // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 404 Not Found if patient not found
        }
    }

    // DELETE: Delete a patient by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            clinicService.deletePatient(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content for successful delete
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 404 Not Found if patient not found
        }
    }


}
