package com.clinic.patient.repositories;

import com.clinic.patient.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DoctorRepository   extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);
}
