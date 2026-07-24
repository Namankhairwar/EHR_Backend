package com.clinic.patient.doctor.controller;


import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.appointment.dto.DoctorScheduleDTO;
import com.clinic.patient.doctor.dto.DoctorResponse;
import com.clinic.patient.doctor.dto.DoctorSummaryDTO;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.repositories.DoctorRepository;
import com.clinic.patient.doctor.service.DoctorService;
import com.clinic.patient.user.dto.UserRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
@AllArgsConstructor
public class DoctorController {


    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;



    // Get All Doctors
    @GetMapping("/all")
    public ResponseEntity<List<DoctorSummaryDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctorSummaries());
    }

    // Get Doctor by ID
    @GetMapping("/{id}/profile")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable String id) {
        return ResponseEntity.ok( MAP.map(doctorService.getDoctorById(id).orElseThrow(),
                DoctorResponse::new));

    }

    @PreAuthorize("hasRole('ADMIN') or @accessGuard.isSelf(#id)")
    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateDoctor(@PathVariable String id, @RequestBody UserRequestDTO userRequestDTO) {
        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isEmpty()) {
            return GlobalExceptionHandler.notFound(new Exception("There is no doctor register with this ehrId"));
        }
        try {
            Doctor doctor = existingDoctorOpt.get();
            MAP.copyInTheObject(userRequestDTO, doctor.getUser(), "role", "password");
            MAP.copyInTheObject(userRequestDTO, doctor);
            doctorRepository.save(doctor);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return GlobalExceptionHandler.incorrectUpdate(e);
        }
    }



    @PreAuthorize("hasRole('ADMIN') or @accessGuard.isSelf(#id)")
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updateDoctorPassword(@PathVariable String id, @RequestBody UserRequestDTO userRequestDTO) {
        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isEmpty() || userRequestDTO.getPassword() == null || userRequestDTO.getPassword().isEmpty()) {
            return GlobalExceptionHandler.incorrectUpdate(new Exception("Doctor not found or password missing"));
        }
        Doctor doctor = existingDoctorOpt.get();
        doctor.getUser().setPassword(userRequestDTO.getPassword());
        doctorRepository.save(doctor);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @accessGuard.isSelf(#id)")
    @PutMapping("/{id}/degrees")
    public ResponseEntity<?> updateDoctorDegrees(@PathVariable String id, @RequestBody UserRequestDTO userRequestDTO) {
        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isEmpty() || userRequestDTO.getDoctorProfile() == null) {
            return GlobalExceptionHandler.incorrectUpdate(new Exception("Doctor not found or degrees missing"));
        }
        Doctor doctor = existingDoctorOpt.get();
        if (doctor.getDoctorProfile() == null) {
            doctor.setDoctorProfile(userRequestDTO.getDoctorProfile());
        } else {
            doctor.getDoctorProfile().setDegrees(userRequestDTO.getDoctorProfile().getDegrees());
        }
        return ResponseEntity.ok(doctorRepository.save(doctor));
    }


    // Delete Doctor
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable String id) {
      try {
          doctorService.deleteDoctor(id);
          return ResponseEntity.noContent().build();
      }catch(Exception e){
          return  GlobalExceptionHandler.notFound(new Exception("There is no doctor register with this ehrId"));
      }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorSummaryDTO>> searchDoctors(@RequestParam("query") String query) {
        return ResponseEntity.ok(doctorService.searchDoctorSummaries(query));
    }

    // Weekly availability: readable by anyone logged in (patients need it to book)
    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<DoctorScheduleDTO>> getSchedule(@PathVariable String id) {
        return ResponseEntity.ok(doctorService.getSchedule(id));
    }

    // Only the doctor (or an admin) can set their availability
    @PreAuthorize("hasRole('ADMIN') or @accessGuard.isSelf(#id)")
    @PutMapping("/{id}/schedule")
    public ResponseEntity<?> setSchedule(@PathVariable String id, @RequestBody List<DoctorScheduleDTO> schedule) {
        try {
            doctorService.setSchedule(id, schedule);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
