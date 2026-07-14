package com.clinic.patient.patient.entity;

import com.clinic.patient.patient.state.AllergyType;
import com.clinic.patient.patient.state.Severity;
import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patient_allergies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allergyId;

    @Column(name = "allergen_name", nullable = false)
    private String allergenName;

    @Enumerated(EnumType.STRING)
    @Column(name = "allergy_type", nullable = false)
    private AllergyType allergyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Column(nullable = false)
    private String reaction;

    @Column(name = "diagnosed_on", nullable = false)
    private LocalDate diagnosedOn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;
}