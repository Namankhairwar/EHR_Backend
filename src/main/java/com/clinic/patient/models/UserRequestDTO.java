package com.clinic.patient.models;

import com.clinic.patient.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Data
public class UserRequestDTO {
    private String fullName;
    private String email;
    private String password;
    private Role role;
}
