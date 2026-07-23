package com.clinic.patient.doctor.entity;

import com.clinic.patient.applicationCommonFeature.mapping.db.StringConvert;
import com.clinic.patient.appointment.entity.AppointmentTimeDoctor;
import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

/**
 * @author Krishana dubey
 */
@Entity
@Table(name = "doctors")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    private String id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "ehrid", name = "ehrid")
    @MapsId
    private User user;

    @Embeddable
    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorProfile {

        // Explicitly added Aadhaar and PAN for the Doctor
        @Column(name = "aadhaar_number", unique = true,length = 12)
        private String aadhaarNumber;

        @Column(name = "pan_number", unique = true)
        private String panNumber;

        private String specialization;

        private String licenseNumber;

        @Column(columnDefinition = "TEXT")
        private String aboutDoctor;

        @Convert(converter = StringConvert.class)
        @Column(columnDefinition = "TEXT")
        private List<String> degrees;

        // Consultation fee shown to patients when booking (display only).
        private Integer consultationFee;
    }

    @Embedded
    private DoctorProfile doctorProfile;

    @OneToMany(mappedBy = "doctor")
    private List<AppointmentTimeDoctor> appointmentTimeDoctor;
}