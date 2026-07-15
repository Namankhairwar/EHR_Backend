package com.clinic.patient.appointment.dto;

import com.clinic.patient.appointment.state.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {

    private long token;
    private long patientId;
    private long doctorId;
    private String date;
    private String startTime;
    private String lastTime;
    private Long charge;
    private AppointmentStatus status;
    private Long total_seat;
    private Long available_seat;
}
