package com.clinic.patient.appointment.repositories;

import com.clinic.patient.appointment.dto.AppointmentRequestDTO;
import com.clinic.patient.appointment.dto.AppointmentResponseDTO;
import com.clinic.patient.appointment.entity.Appointment;
import com.clinic.patient.user.entity.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    List<Appointment> getAllByPatient_Id(Long patientId, Pageable pageable);
    Optional<Appointment> getAppointmentById(Long id);


  int countAppointmentsByPatient_IdAndStatus_SCHEDULEDOrderByAppointmentTimeDesc(Long id);

    List<Appointment> getAllByPatient_IdAndStatus_SCHEDULEDOrderByAppointmentTimeDesc(Long patientId);


}
