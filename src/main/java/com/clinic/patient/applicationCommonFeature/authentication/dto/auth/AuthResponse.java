package com.clinic.patient.applicationCommonFeature.authentication.dto.auth;


import com.clinic.patient.applicationCommonFeature.authentication.dto.jwt.TokenResponse;
import com.clinic.patient.user.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@AllArgsConstructor
@Getter
@Setter
@ResponseBody
public class AuthResponse {
    UserResponseDTO userResponseDTO;
    TokenResponse tokenResponse;
}
