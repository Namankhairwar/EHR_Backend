package com.clinic.patient.report.entity;

import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDate dateTime;
    private String info;

    @Column(name = "description", length = 2000)
    private String desc;

    private String conclusion;

    @Lob
    @Column(name = "file_data", columnDefinition = "BYTEA")
    private byte[] fileData;

    private String fileName;
    private String fileType;
}
