package com.clinic.patient.medication.dto;

import com.clinic.patient.medication.entity.DietInstruction;
import com.clinic.patient.medication.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicineResponse {

    private long id;
    private String prescribedBy;

    private String prescribedOn;

    private List<Medicine.MedicineDetails> details;

    private String patient;

    private DietInstruction diet;
}
