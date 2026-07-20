package com.clinic.patient.report.entity;

import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.report.state.ReportAccessStatus;
import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_access_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportAccessRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportAccessStatus status;

    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
}
