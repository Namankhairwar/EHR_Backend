package com.clinic.patient.applicationCommonFeature.authentication.dto.login;


import lombok.*;

/**
 * @author Krishana dubey
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}