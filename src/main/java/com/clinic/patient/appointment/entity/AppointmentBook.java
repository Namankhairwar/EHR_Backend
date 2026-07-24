package com.clinic.patient.appointment.entity;

import com.clinic.patient.appointment.state.AppointmentStatus;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentBook {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long token;

    // sequential queue number per doctor per date: 1, 2, 3...
    private Integer tokenNumber;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = AppointmentTime.class)
    @JoinColumn(name = "appointment_time_id", nullable = false)
    private AppointmentTime appointmentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "ehrId")
    private User patient;
}
