package com.clinic.patient.user.dto;

import lombok.Data;
import java.util.List;

@Data
public class RestrictionRequestDto {
    private String patientId;
    private String accessorId;
    private List<String> restrictedAttributes;
}
