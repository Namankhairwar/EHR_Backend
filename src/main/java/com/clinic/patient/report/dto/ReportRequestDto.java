package com.clinic.patient.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {
    private List<Long> patientIds;
    private Long doctorId;
    private String dateTime;
    private String info;
    private String desc;
    private String conclusion;
    private String fileBase64;
    private String fileName;
    private String fileType;
}
