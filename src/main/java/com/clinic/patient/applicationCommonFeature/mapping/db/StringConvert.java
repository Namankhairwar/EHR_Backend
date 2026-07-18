package com.clinic.patient.applicationCommonFeature.mapping.db;

import jakarta.persistence.Converter;

@Converter
public class StringConvert extends Convertor<String>{
    public StringConvert(){
        super(String.class);
    }
}
