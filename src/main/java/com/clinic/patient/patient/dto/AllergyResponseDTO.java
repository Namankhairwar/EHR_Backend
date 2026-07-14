package com.clinic.patient.patient.dto;

import com.clinic.patient.patient.state.Severity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllergyResponseDTO {

    private Long id;

    private String allergenName;

    private Severity severity;

    private String reaction;

    private LocalDate diagnosedOn;
}