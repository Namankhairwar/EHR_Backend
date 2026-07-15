//package com.clinic.patient.appointment.entity;
//
//import com.clinic.patient.appointment.state.AppointmentStatus;
//import com.clinic.patient.doctor.entity.Doctor;
//import com.clinic.patient.user.entity.User;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "appointment")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class AppointmentBook {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private long token;
//
//    @ManyToOne(cascade = CascadeType.PERSIST ,fetch = FetchType.LAZY , targetEntity = AppointmentTime.class)
//    @JoinColumn(name= "id" ,nullable = false  , updatable = false)
//    private AppointmentTime appointmentTime;
//
//    @OneToOne
//    @JoinColumn(name="doctor_id")
//    private Doctor doctorId;
//
//    @Enumerated(EnumType.STRING)
//    @JoinColumn(name="status")
//    private AppointmentStatus status;
//
//
//    @OneToOne
//    @JoinColumn(name = "patient_id" ,referencedColumnName = "ehrId")
//    private User patientId;
//
//}
