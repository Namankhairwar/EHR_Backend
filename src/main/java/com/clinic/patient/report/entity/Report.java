//package com.clinic.patient.report.entity;
//
//import com.clinic.patient.doctor.entity.Doctor;
//import com.clinic.patient.user.entity.Patient;
//import jakarta.persistence.*;
//import lombok.Data;
//import org.springframework.core.io.Resource;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.sql.Blob;
//import java.time.LocalDate;
//
//@Entity
//@Data
//public class Report {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private long id;
//
//    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , targetEntity = Patient.class)
//    @JoinColumn(unique = true, nullable = false )
//    private Patient patient_id;
//    @JoinColumn(unique = true, nullable = false )
//    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , targetEntity = Doctor.class)
//    private Doctor doctor_id;
//
//    private LocalDate date_time;
//    private String info;
//    private String desc;
//    private String conclusion;
//    @Column(unique=true , nullable = false , columnDefinition = "longblob")
//    private Resource report;
//
//
//}
