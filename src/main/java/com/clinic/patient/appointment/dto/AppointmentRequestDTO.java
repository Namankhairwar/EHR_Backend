package com.clinic.patient.appointment.dto;

import com.clinic.patient.appointment.state.AppointmentStatus;
import lombok.Data;

@Data
public class AppointmentRequestDTO {

    private String patientId;
    private String doctorId;
    private AppointmentStatus status;
    private String date;
    private String startTime;
    private String lastTime;
    private Long charge;


}