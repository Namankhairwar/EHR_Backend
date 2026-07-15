package com.clinic.patient.medication.entity;

import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DietInstruction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(unique=true)
    private String name_of_diet; // for general,sugar,fitness, or any other diagnose so this same data not going to be entered
   private String food_description;

   @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , targetEntity = Doctor.class)
   @JoinColumn(name="doc_id" , referencedColumnName = "ehrid")
    private Doctor prescribeBy;


   @ManyToOne(targetEntity = User.class)
   @JoinColumn(name= "patientId",referencedColumnName ="ehrid" )

    private User patientId;
}
