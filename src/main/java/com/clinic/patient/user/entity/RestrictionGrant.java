package com.clinic.patient.user.entity;

import com.clinic.patient.user.state.RestrictionGrantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "restriction_grants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestrictionGrant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accessor_id", nullable = false)
    private User accessor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestrictionGrantStatus status;

    @Column(length = 1000)
    private String restrictedAttributes;

    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
}
