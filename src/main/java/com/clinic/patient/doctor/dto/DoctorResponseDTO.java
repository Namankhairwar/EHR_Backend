package com.clinic.patient.doctor.dto;

import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Krishana dubey
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ResponseBody
public class DoctorResponseDTO {
    private String name;
    private String specialization;
    private String phone;
}
