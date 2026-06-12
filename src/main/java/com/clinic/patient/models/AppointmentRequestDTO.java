package com.clinic.patient.models;

import com.clinic.patient.role.AppointmentStatus;
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