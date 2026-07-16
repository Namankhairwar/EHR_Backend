package com.clinic.patient.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {
    private long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String dateTime;
    private String info;
    private String desc;
    private String conclusion;
    private String fileName;
    private String fileType;
}
