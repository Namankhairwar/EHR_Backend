package com.clinic.patient.models;


import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

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