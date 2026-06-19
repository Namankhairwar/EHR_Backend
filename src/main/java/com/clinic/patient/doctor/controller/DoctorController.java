package com.clinic.patient.doctor.controller;


import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {


    private final DoctorService doctorService;
    @Autowired
    DoctorController(DoctorService doctorService){
        this.doctorService =doctorService;
    }



    // Get All Doctors
    @GetMapping("/all")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // Get Doctor by ID
    @GetMapping("/{id}/profile")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
      try{
        return  ResponseEntity.ok(doctorService.updateDoctor(id,doctor));
      }catch(Exception e){
        return  GlobalExceptionHandler.incorrectUpdate(new ClassNotFoundException("try again later"));
      }

    }

    // Delete Doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
      try {
          doctorService.deleteDoctor(id);
          return ResponseEntity.noContent().build();
      }catch(Exception e){
          return  GlobalExceptionHandler.incorrectUpdate(new NoSuchFieldException("There is no doctor register with this ehrId"));
      }
    }
}
