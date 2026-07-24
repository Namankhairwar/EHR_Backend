package com.clinic.patient.user.dto;

import com.clinic.patient.user.state.RestrictionGrantStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RestrictionGrantResponseDto {
    private Long id;
    private String patientId;
    private String patientName;
    private String accessorId;
    private String accessorName;
    private RestrictionGrantStatus status;
    private List<String> restrictedAttributes;
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
}
