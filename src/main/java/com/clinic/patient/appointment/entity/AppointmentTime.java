package com.clinic.patient.appointment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appointment_Time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime lastTime;

    @OneToMany(mappedBy = "appointmentTime", targetEntity = AppointmentBook.class)
    private List<AppointmentBook> appointmentBook;

    private Long charge;

    private Long total_seat;
    private Long available_seat;
}