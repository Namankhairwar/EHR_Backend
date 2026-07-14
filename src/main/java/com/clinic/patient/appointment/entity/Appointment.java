package com.clinic.patient.appointment.entity;

import com.clinic.patient.appointment.state.AppointmentStatus;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity =User.class)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Doctor.class)
    @JoinColumn(name = "doctor_id", nullable = false )
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;


}
