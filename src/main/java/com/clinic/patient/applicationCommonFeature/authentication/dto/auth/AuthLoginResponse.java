package com.clinic.patient.applicationCommonFeature.authentication.dto.auth;

import com.clinic.patient.applicationCommonFeature.authentication.dto.login.LoginResponse;
import com.clinic.patient.applicationCommonFeature.authentication.dto.jwt.TokenResponse;
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
