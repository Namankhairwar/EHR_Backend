package com.clinic.patient.doctor.controller;


import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.doctor.dto.DoctorResponse;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.service.DoctorService;
import com.clinic.patient.user.dto.UserRequestDTO;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import com.clinic.patient.doctor.repositories.DoctorRepository;

@RestController
@RequestMapping("/api/doctors")
@AllArgsConstructor
public class DoctorController {


    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;



    // Get All Doctors
    @GetMapping("/all")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // Get Doctor by ID
    @GetMapping("/{id}/profile")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok( MAP.map(doctorService.getDoctorById(id).orElseThrow(),
                DoctorResponse::new));

    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
      try{
         Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isPresent()) {

       MAP.copyInTheObject(userRequestDTO,existingDoctorOpt.get().getUser(),"role","password");
          MAP.copyInTheObject(userRequestDTO,existingDoctorOpt.get());
            doctorService.updateDoctor(id,existingDoctorOpt.get());
        return  ResponseEntity.ok().build();
      }else{
              return  GlobalExceptionHandler.incorrectUpdate(new ClassNotFoundException("try again later"));  
      }
    }catch(Exception e){
        return  GlobalExceptionHandler.incorrectUpdate(new ClassNotFoundException("try again later"));
      }

    }



       @PatchMapping("/{id}/password")
    public ResponseEntity<?> updateDoctorPassword(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
      try{
         Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isPresent()) {

       MAP.copyInTheObjectRequired(userRequestDTO,existingDoctorOpt.get().getUser(),"password");
       return ResponseEntity.ok().build();
        } 
              return  GlobalExceptionHandler.incorrectUpdate(new ClassNotFoundException("try again later"));  
    
    }catch(Exception e){
        return  GlobalExceptionHandler.incorrectUpdate(new ClassNotFoundException("try again later"));
      }

    }

      @PutMapping("/{id}/degrees")
    public ResponseEntity<?> updateDoctorDegrees(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
      try{
         Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isPresent()) {
          
          MAP.copyInTheObjectRequired(userRequestDTO,existingDoctorOpt.get(),"doctorProfile");
        return  ResponseEntity.ok(doctorService.updateDoctor(id,existingDoctorOpt.get()));
      }else{
              return  GlobalExceptionHandler.incorrectUpdate(new ClassNotFoundException("try again later"));  
      }
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
