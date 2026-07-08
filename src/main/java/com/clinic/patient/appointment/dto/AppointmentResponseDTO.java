package com.clinic.patient.appointment.dto;

import com.clinic.patient.appointment.state.AppointmentStatus;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.patient.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {
    private Long id;
    private Patient patient;
    private Doctor doctor;
    private String appointmentTime;
    private Integer duration;
    private String reason;
    private AppointmentStatus status;
}
