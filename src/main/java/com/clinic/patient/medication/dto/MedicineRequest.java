package com.clinic.patient.medication.dto;

import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.medication.entity.DietInstruction;
import com.clinic.patient.medication.entity.Medicine;
import com.clinic.patient.user.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class MedicineRequest {

    private long prescribedBy;

    private String prescribedOn;

    private List<Medicine.MedicineDetails> details;

    private long patient;

    private DietInstruction diet;

}
