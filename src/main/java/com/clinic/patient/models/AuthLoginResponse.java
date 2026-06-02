package com.clinic.patient.models;

import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

@AllArgsConstructor
@ResponseBody
@Data
@Builder
@Getter
@Setter
public class AuthLoginResponse {
    LoginResponse loginResponse;
    TokenResponse tokenResponse;
}
