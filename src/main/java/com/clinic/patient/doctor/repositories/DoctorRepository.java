package com.clinic.patient.doctor.repositories;

import com.clinic.patient.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
@EnableJpaRepositories(basePackages = "com.clinic.patient.doctor.repositories")
public interface DoctorRepository   extends JpaRepository<Doctor, Long> {


}
