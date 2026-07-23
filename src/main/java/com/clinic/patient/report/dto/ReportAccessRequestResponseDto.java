package com.clinic.patient.report.dto;

import com.clinic.patient.report.state.ReportAccessStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportAccessRequestResponseDto {
    private Long id;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private ReportAccessStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
}
