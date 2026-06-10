package com.clinic.patient.dto;

import com.clinic.patient.models.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentResponseDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private LocalDateTime appointmentTime;
    private Integer duration;
    private String reason;
    private AppointmentStatus status;
}
