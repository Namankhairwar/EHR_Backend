package com.clinic.patient.user.entity;

import com.clinic.patient.user.state.Gender;
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

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private String mobile;

    @Column
    private String address;

    @Column
    private int age;

    @Column
    private Gender gender;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "patient_allergies",
            joinColumns = @JoinColumn(name = "patient_id")
    )
    @Column(name = "allergy")
    private List<String> allergies;



}
