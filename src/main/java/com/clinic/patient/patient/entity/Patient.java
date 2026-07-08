package com.clinic.patient.patient.entity;

import com.clinic.patient.applicationCommonFeature.mapping.db.StringConvert;
import com.clinic.patient.patient.state.AllergyType;
import com.clinic.patient.patient.state.Severity;
import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;




@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne(targetEntity = User.class)
    private User user;


    @Column(name = "allergy")
    @Convert(converter = StringConvert.class)
    private List<String> allergies;
    

}