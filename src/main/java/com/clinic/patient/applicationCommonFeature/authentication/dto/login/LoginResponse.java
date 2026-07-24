package com.clinic.patient.applicationCommonFeature.authentication.dto.login;

import com.clinic.patient.user.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Krishana dubey
 */
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
