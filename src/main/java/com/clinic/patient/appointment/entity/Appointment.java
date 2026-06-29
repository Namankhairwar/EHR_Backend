package com.clinic.patient.appointment.entity;

import com.clinic.patient.appointment.state.AppointmentStatus;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.user.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column
    private String cancellationReason;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "appointment_reschedule_history",
            joinColumns = @JoinColumn(name = "appointment_id")
    )
    @Column(name = "rescheduled_time")
    private List<LocalDateTime> rescheduleHistory;


}
