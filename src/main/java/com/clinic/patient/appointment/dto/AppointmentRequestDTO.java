package com.clinic.patient.appointment.dto;

import com.clinic.patient.appointment.state.AppointmentStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {

    private Long patientId;
    private Long doctorId;
    private String appointmentTime;
    private Integer duration;
    private String reason;
    private AppointmentStatus status;

}