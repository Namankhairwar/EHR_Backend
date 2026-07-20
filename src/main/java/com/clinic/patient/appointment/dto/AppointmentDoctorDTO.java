package com.clinic.patient.appointment.dto;
import com.clinic.patient.appointment.state.Days;
import lombok.Data;

@Data

public class AppointmentDoctorDTO {
    private String doctorId;

    static class DoctorAppointmentData{

    }
    private Days status;
    private String startTime;
    private String lastTime;
    private Long charge;
    private Long per_patient;

}
