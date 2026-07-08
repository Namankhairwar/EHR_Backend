package com.clinic.patient.patient.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientAllergiesDTO {
    private List<String> allergies;
}