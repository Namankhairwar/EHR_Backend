package com.clinic.patient.user.dto;

import com.clinic.patient.applicationCommonFeature.state.Role;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.state.BloodGroup;
import com.clinic.patient.user.state.Gender;
import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Builder
@AllArgsConstructor
@Data
@Setter
@NoArgsConstructor
@ResponseBody
public class UserResponseDTO {
   private String email;
   private String ehrId;
   private String firstName;
   private String lastName;
   private Long phoneNo;
   private String dob;
   private Gender gender;
   private Role role;
   private BloodGroup bloodGroup;
   private User.Address address;
   private Doctor.DoctorProfile doctorProfile;
   private String maritalStatus;
   private User.Emergency emergencyContact;
}

