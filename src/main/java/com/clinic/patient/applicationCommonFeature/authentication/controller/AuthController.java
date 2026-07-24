package com.clinic.patient.applicationCommonFeature.authentication.controller;

import com.clinic.patient.applicationCommonFeature.authentication.dto.auth.AuthResponse;
import com.clinic.patient.applicationCommonFeature.authentication.dto.jwt.TokenResponse;
import com.clinic.patient.applicationCommonFeature.authentication.dto.login.LoginRequest;
import com.clinic.patient.applicationCommonFeature.authentication.entity.VerificationToken;
import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.applicationCommonFeature.state.Role;
import com.clinic.patient.doctor.service.DoctorService;
import com.clinic.patient.security.jwt.JwtService;
import com.clinic.patient.user.dto.UserRequestDTO;
import com.clinic.patient.user.dto.UserResponseDTO;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import com.clinic.patient.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

import com.clinic.patient.applicationCommonFeature.authentication.service.EmailService;
import com.clinic.patient.applicationCommonFeature.authentication.service.VerificationTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {


    private final JwtService jwtService;
    private final DoctorService doctorService;
    private final UserService userService;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;
    private final UserRepository userRepository;
    @Autowired
    public AuthController(JwtService jwtService ,DoctorService doctorService , UserService userService,
      VerificationTokenService verificationTokenService,EmailService em, UserRepository userRepository){
        this.doctorService =doctorService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.emailService = em;
        this.userRepository = userRepository;
        this.verificationTokenService = verificationTokenService;

    }
    /**
     *
     * @param dto  object uses to register the user
     * @return UserResponseDTO object
     */

    @PostMapping("register")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO dto) {
        try {
            if (Role.PATIENT.equals(dto.getRole())) {
                return userService.register(dto);
            }
            if (Role.DOCTOR.equals(dto.getRole())) {
                return doctorService.saveDoctor(dto);
            }else{
                throw new RuntimeException();
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GlobalExceptionHandler.internalError(new RuntimeException(e.getLocalizedMessage())));
        }
    }


    /**
     *
     * @param loginRequest  is used to validate a user by this response body
     * @return UserResponseDTO object
     *
     */
    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
       try {
            return userService.login(loginRequest);
        }
       catch (Exception e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .body(GlobalExceptionHandler.internalError(new RuntimeException("Server is not accepting response try again after some time")));
       }

//       catch (Exception e) {
//
//           e.printStackTrace();
//
//           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                   .body(e.getMessage());
//       }
    }


    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {

        try {

            verificationTokenService.verifyToken(token);

            return ResponseEntity.ok(
                    "Email verified successfully. You can login now."
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/resend")
    public ResponseEntity<String> resendEmail(@RequestBody String email){
       return userService.resend(email);
    }
    /**
     * @param body contains the refreshToken issued at login
     * @return a fresh access + refresh token pair
     */
    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String email = jwtService.extractEmail(body.getOrDefault("refreshToken", ""), "Refresh");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or Expired token");
        }
        return ResponseEntity.ok(new TokenResponse(jwtService.generateNewToken(email), jwtService.generateRefreshToken(email)));
    }
}