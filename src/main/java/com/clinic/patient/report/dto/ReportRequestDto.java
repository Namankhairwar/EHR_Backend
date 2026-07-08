package com.clinic.patient.report.dto;

import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.patient.entity.Patient;

import java.io.InputStream;

public class ReportRequestDto {
    private long id;
    private Patient patient_id;
    private Doctor doctor_id;
    private String date_time;
    private String info;
    private String desc;
    private String conclusion;
    private InputStream report;
}
