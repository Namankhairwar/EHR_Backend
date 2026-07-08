package com.clinic.patient.patient.dto;

import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.state.BloodGroup;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientOverviewResponseDTO {
    private User.Emergency emergencyContact;
    private List<String> allergies;
    private BloodGroup bloodGroup;
}