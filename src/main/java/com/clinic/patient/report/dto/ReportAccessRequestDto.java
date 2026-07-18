package com.clinic.patient.report.dto;

import lombok.Data;

@Data
public class ReportAccessRequestDto {
    private String doctorId;
    private String patientId;
}
