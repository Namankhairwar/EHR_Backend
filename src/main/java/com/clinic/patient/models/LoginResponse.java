package com.clinic.patient.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Builder
@Setter
@AllArgsConstructor
@ResponseBody
public class LoginResponse {
    boolean status;
    String message;
    UserResponseDTO userResponseDTO;
}
