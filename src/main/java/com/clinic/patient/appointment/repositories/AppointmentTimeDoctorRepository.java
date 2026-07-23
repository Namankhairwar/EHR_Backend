package com.clinic.patient.appointment.repositories;

import com.clinic.patient.appointment.entity.AppointmentTimeDoctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentTimeDoctorRepository extends JpaRepository<AppointmentTimeDoctor, Long> {

    List<AppointmentTimeDoctor> findAllByDoctor_Id(String doctorId);

    void deleteAllByDoctor_Id(String doctorId);
}
