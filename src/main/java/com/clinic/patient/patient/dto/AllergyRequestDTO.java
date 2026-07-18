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

    private String category;

    private String items;
    private AllergyType allergyType;

    private Severity severity;

    private String description;
}