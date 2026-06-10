package com.clinic.patient.repositories;

import com.clinic.patient.dto.AppointmentRequestDTO;
import com.clinic.patient.dto.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);
    List<AppointmentResponseDTO> getAllAppointments();
    AppointmentResponseDTO getAppointmentById(Long id);
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto);
    AppointmentResponseDTO cancelAppointment(Long id);
    void deleteAppointment(Long id);

}
