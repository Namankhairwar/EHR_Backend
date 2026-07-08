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


    @OneToOne
    @JoinColumn(name = "ehr_id")
    @MapsId
    private User user;

//    @Column
//    private List<Allergy> allergy;
//
//    @ToString
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Data
//    @Converter
//    static class Allergy{
//
//    }

}
