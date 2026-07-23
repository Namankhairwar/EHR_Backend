package com.clinic.patient.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Public-safe doctor listing: no Aadhaar/PAN, no personal user data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSummaryDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String aboutDoctor;
    private List<String> degrees;
    private Integer consultationFee;
}
