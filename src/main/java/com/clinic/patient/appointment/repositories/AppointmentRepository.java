package com.clinic.patient.appointment.repositories;

import com.clinic.patient.appointment.entity.Appointment;
import com.clinic.patient.appointment.state.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient_Id(Long patientId);

    List<Appointment> findByPatient_IdAndStatus(Long patientId, AppointmentStatus status);

    List<Appointment> findByPatient_IdAndAppointmentTimeAfter(Long patientId, LocalDateTime now);

    List<Appointment> findByDoctor_Id(Long doctorId);

    List<Appointment> findByDoctor_IdAndStatus(Long doctorId, AppointmentStatus status);

    List<Appointment> findByDoctor_IdAndAppointmentTimeBetween(
            Long doctorId,
            LocalDateTime start,
            LocalDateTime end
    );



}
