package com.clinic.patient.models;


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
