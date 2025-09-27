package com.clinic.patient.repositories;

import com.clinic.patient.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository   extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);
}
