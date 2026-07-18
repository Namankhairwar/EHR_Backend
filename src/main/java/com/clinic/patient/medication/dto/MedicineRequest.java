package com.clinic.patient.medication.dto;

import com.clinic.patient.medication.entity.DietInstruction;
import com.clinic.patient.medication.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class MedicineRequest {

    private String prescribedBy;

    private String prescribedOn;

    private List<Medicine.MedicineDetails> details;

    private String patient;

    private DietInstruction diet;

}
