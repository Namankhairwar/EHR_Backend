package com.clinic.patient.repositories;

import com.clinic.patient.models.AppointmentRequestDTO;
import com.clinic.patient.models.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);
    List<AppointmentResponseDTO> getAllAppointments();
    AppointmentResponseDTO getAppointmentById(Long id);
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto);
    AppointmentResponseDTO cancelAppointment(Long id);
    void deleteAppointment(Long id);

}
