package com.clinic.patient.appointment.repositories;

import com.clinic.patient.appointment.entity.Appointment;
import com.clinic.patient.appointment.state.AppointmentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    List<Appointment> getAllByPatient_Id(Long patientId, Pageable pageable);
    Optional<Appointment> getAppointmentById(Long id);


  int countAppointmentsByPatient_IdAndStatusOrderByAppointmentTimeDesc(Long id, AppointmentStatus status);

    List<Appointment> getAllByPatient_IdAndStatusOrderByAppointmentTimeDesc(Long patientId, AppointmentStatus status);


}
