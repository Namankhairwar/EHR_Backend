package com.clinic.patient.appointment.service;

import com.clinic.patient.appointment.dto.AppointmentRequestDTO;
import com.clinic.patient.appointment.dto.AppointmentResponseDTO;
import com.clinic.patient.appointment.state.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;
public interface AppointmentService{

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);

    AppointmentResponseDTO getAppointmentById(Long appointmentId);

    AppointmentResponseDTO rescheduleAppointment(Long appointmentId, AppointmentRequestDTO dto);

    AppointmentResponseDTO cancelAppointment(Long appointmentId, AppointmentRequestDTO dto);

    List<AppointmentResponseDTO> getAppointmentsByPatient(
            Long patientId,
            AppointmentStatus status,
            Boolean upcoming
    );

    List<AppointmentResponseDTO> getAppointmentsByDoctor(
            Long doctorId,
            AppointmentStatus status,
            LocalDate date
    );

}
