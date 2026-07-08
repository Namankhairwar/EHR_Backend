package com.clinic.patient.doctor.dto;

import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.medication.entity.Medicine;
import com.clinic.patient.user.entity.User;
import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

@Builder
@AllArgsConstructor
@Data
@Setter
@NoArgsConstructor
public class DoctorResponse {

    private Long id;
    private User user;
      private Doctor.DoctorProfile doctorProfile;

        private Medicine medicine;

}
