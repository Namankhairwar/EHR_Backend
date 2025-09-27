package com.clinic.patient.service;


import com.clinic.patient.entities.Doctor;
import com.clinic.patient.exception.DoctorNotFoundException;
import com.clinic.patient.repositories.DoctorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {   //constructor injection
        this.doctorRepository = doctorRepository;
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
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

    public Doctor updateDoctor(String email, Doctor doctor) {
        Optional<Doctor> existingDoctorOpt = doctorRepository.findByEmail(email);
        if (existingDoctorOpt.isPresent()) {
            Doctor existingDoctor = existingDoctorOpt.get();
            existingDoctor.setName(doctor.getName());
            existingDoctor.setSpecialization(doctor.getSpecialization());
            existingDoctor.setPhone(doctor.getPhone());
            existingDoctor.setEmail(doctor.getEmail());
            existingDoctor.setAge(doctor.getAge());
            existingDoctor.setExperience(doctor.getExperience());
            existingDoctor.setGender(doctor.getGender());
            return doctorRepository.save(existingDoctor);
        } else {
            throw new DoctorNotFoundException("Doctor not found with email: " + email);
        }
    }
}
