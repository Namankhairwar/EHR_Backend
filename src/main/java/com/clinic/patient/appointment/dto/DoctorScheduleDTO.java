package com.clinic.patient.appointment.dto;

import com.clinic.patient.appointment.state.Days;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDTO {

    private Days day;
    private String startTime; // HH:mm:ss
    private String endTime;   // HH:mm:ss
}
