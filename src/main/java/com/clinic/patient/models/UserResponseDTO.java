package com.clinic.patient.models;

import com.clinic.patient.role.Role;
import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

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
   private Role role;

}
