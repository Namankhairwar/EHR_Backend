package com.clinic.patient.appointment.dto;

import com.clinic.patient.appointment.state.AppointmentStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {

    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentTime;
    private Integer durationMinutes;
    private String reason;
    private AppointmentStatus status;
    private String cancellationReason;

}