package com.clinic.patient.applicationCommonFeature.authentication.dto.jwt;

import org.springframework.stereotype.Component;

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
