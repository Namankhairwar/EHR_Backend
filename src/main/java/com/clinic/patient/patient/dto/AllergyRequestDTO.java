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
public class AllergyRequestDTO {

    private String allergenName;

    private AllergyType allergyType;

    private Severity severity;

    private String reaction;

    private String diagnosedOn;
}