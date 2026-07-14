package com.clinic.patient.patient.dto;

import com.clinic.patient.patient.state.AllergyType;
import com.clinic.patient.patient.state.Severity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllergyResponseDTO {

    private Long allergyId;

    private String allergenName;

    private Severity severity;

    private String reaction;

    private String diagnosedOn;
    private AllergyType allergyType;
}