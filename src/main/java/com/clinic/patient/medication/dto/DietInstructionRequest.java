package com.clinic.patient.medication.dto;

import com.clinic.patient.doctor.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietInstructionRequest {

    private String name_of_diet; // for general,sugar,fitness, or any other diagnose so this same data not going to be entered
    private String food_description;


    private long prescribeBy;

}
