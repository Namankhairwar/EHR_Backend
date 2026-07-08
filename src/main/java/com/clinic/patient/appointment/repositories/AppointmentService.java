package com.clinic.patient.appointment.repositories;

import com.clinic.patient.appointment.dto.AppointmentRequestDTO;
import com.clinic.patient.appointment.dto.AppointmentResponseDTO;
import java.util.List;
public interface AppointmentService{

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);
    List<AppointmentResponseDTO> getAllAppointments();
    AppointmentResponseDTO getAppointmentById(Long id);
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto);
    AppointmentResponseDTO cancelAppointment(Long id);
    void deleteAppointment(Long id);

}
