package com.clinic.patient.user.dto;

import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.user.state.BloodGroup;
import com.clinic.patient.user.state.Gender;
import com.clinic.patient.applicationCommonFeature.state.Role;
import lombok.*;
import com.clinic.patient.user.entity.User;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Data
@Setter
@NoArgsConstructor
@ResponseBody
public class UserResponseDTO {
   private String email;
   private Long id;
   private String firstName;
   private String lastName;
   private long phoneNo;
   private LocalDate dob;
   private Gender gender;
   private Role role;
   private BloodGroup bloodGroup;
   private User.Address address;
   private Doctor.DoctorProfile doctorProfile;

}
