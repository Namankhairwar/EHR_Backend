package com.clinic.patient.doctor.service;


import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.user.dto.UserRequestDTO;
import com.clinic.patient.user.dto.UserResponseDTO;
import com.clinic.patient.doctor.repositories.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.clinic.patient.user.entity.User;
/**
 * @author Krishana dubey
 *
 * @saveDoctor
 * @getAllDoctors
 * @getDoctorById
 * @deleteDoctor
 * @updateDoctor
 */

@Service
@AllArgsConstructor
public class DoctorService {


    private final DoctorRepository doctorRepository;

    public UserResponseDTO saveDoctor(UserRequestDTO doctor_RequestDTO) {
        Doctor doctor_ehr = new Doctor();
   doctor_ehr.setUser(MAP.map(doctor_RequestDTO,User::new));
        MAP.copyInTheObject(doctor_RequestDTO,doctor_ehr);
    doctor_ehr=doctorRepository.save(doctor_ehr);
     UserResponseDTO userResponseDTO= MAP.map(doctor_RequestDTO, UserResponseDTO::new);
      userResponseDTO.setEhrId(doctor_ehr.getId());
      return userResponseDTO;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    public Doctor updateDoctor(Long id, Doctor doctor)  throws GlobalExceptionHandler{
        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isPresent()) {
            MAP.copyInTheObject(doctor, existingDoctorOpt.get());
            return doctorRepository.save(existingDoctorOpt.get());
        } else {
            throw new GlobalExceptionHandler("Incorrect email try to enter again");
        }
    }
}
