//package com.clinic.patient.appointment.entity;
//
//import com.clinic.patient.appointment.state.Days;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@Entity
//@Table(name = "appointment_Time")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class AppointmentTime {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private long id;
//    @Column
//    private LocalDate date;
//    @Column
//    private LocalTime start_time;
//    @Column
//    private LocalTime end_time;
//
//    @Column
//    private Days days;
//
//    @OneToMany(targetEntity = AppointmentBook.class)
//    @JoinColumn(name = "booking_details", referencedColumnName ="appointmentTime" ,updatable = false  )
//    private List<AppointmentBook> appointmentBook;
//
//    @Column
//    private long doctorCharge_per_patient;
//
//    private long available_slot;
//
//}