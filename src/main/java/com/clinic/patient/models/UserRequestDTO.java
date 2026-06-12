package com.clinic.patient.models;

import com.clinic.patient.role.Role;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserRequestDTO {
    private String fullName;
    private String email;
    private String password;
    private Role role;

}
