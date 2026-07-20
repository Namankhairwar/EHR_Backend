package com.clinic.patient.applicationCommonFeature.authentication.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponse{
    private String accessToken;
    private String refreshToken;
}
