package com.clinic.patient.repositories;

import com.clinic.patient.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository   extends JpaRepository<Doctor, Long> {

}
