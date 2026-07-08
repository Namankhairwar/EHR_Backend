package com.clinic.patient.doctor.entity;

import com.clinic.patient.applicationCommonFeature.mapping.db.StringConvert;
import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;


    @OneToOne(targetEntity = User.class,fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "ehr_id")
    @MapsId
    private User user;



    @Embeddable
    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorProfile{

        private String specialization;
        private String licenseNumber;

        @Convert(converter = StringConvert.class)
        @Column(columnDefinition = "TEXT")
        private List<String> degrees;
    }

    @Embedded
    private DoctorProfile doctorProfile;



}
