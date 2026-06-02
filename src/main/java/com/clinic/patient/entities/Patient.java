package com.clinic.patient.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Krishana dubey
 */
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
    private String gender;

    @Column
    private String allergy;

}
