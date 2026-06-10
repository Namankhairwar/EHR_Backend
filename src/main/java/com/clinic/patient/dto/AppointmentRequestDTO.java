package com.clinic.patient.dto;

import com.clinic.patient.models.AppointmentStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {

    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentTime;
    private Integer duration;
    private String reason;
    private AppointmentStatus status;

}