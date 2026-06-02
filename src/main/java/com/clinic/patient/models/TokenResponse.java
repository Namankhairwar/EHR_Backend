package com.clinic.patient.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Setter
@AllArgsConstructor
@ResponseBody
public class TokenResponse{
    private String generateAccessToken;
    private String generateRefreshToken;
}
