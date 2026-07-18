package com.clinic.patient.applicationCommonFeature.mapping.db;

import com.clinic.patient.medication.entity.Medicine;
import jakarta.persistence.Converter;

@Converter
public class MedicineDetailsConvert extends Convertor<Medicine.MedicineDetails>{
    public MedicineDetailsConvert(){
        super(Medicine.MedicineDetails.class);
    }
}
