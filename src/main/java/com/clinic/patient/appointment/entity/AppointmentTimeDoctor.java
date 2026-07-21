package com.clinic.patient.appointment.entity;

import com.clinic.patient.appointment.state.Days;
import com.clinic.patient.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalTime;

@Entity
@Table(name = "appointment_doctor_time_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentTimeDoctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.MERGE)
    private Doctor doctor;

    private LocalTime startSchedule;

    private LocalTime endSchedule;
    private Days days;


}
