package com.clinic.patient.models;

import com.clinic.patient.role.BloodGroup;
import com.clinic.patient.role.Gender;
import com.clinic.patient.role.Role;
import lombok.*;
import com.clinic.patient.entities.User;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Data
@Setter
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

}
