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


    Optional<Appointment> getAppointmentById(Long id);


  int countAppointmentsByPatient_EhrIdAndStatusOrderByAppointmentTimeDesc(Long id, AppointmentStatus status);

    List<Appointment> getAllByPatient_EhrIdAndStatusOrderByAppointmentTimeDesc(Long patientId, AppointmentStatus status);


    List<Appointment> getAllByPatient_EhrId(Long patientEhrId, Pageable pageable);
}
