package com.clinic.patient.applicationCommonFeature.authentication.controller;

import com.clinic.patient.applicationCommonFeature.authentication.dto.auth.AuthResponse;
import com.clinic.patient.applicationCommonFeature.authentication.dto.login.LoginRequest;
import com.clinic.patient.applicationCommonFeature.authentication.dto.jwt.TokenResponse;
import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.applicationCommonFeature.state.Role;
import com.clinic.patient.security.jwt.JwtService;
import com.clinic.patient.doctor.service.DoctorService;
import com.clinic.patient.user.dto.UserRequestDTO;
import com.clinic.patient.user.dto.UserResponseDTO;
import com.clinic.patient.user.service.UserService;
import com.sun.jdi.InternalException;
import com.clinic.patient.applicationCommonFeature.authentication.service.VerificationTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final JwtService jwtService;
    private final DoctorService doctorService;
    private final UserService userService;

    private final VerificationTokenService verificationTokenService;

    @Autowired
    public AuthController(JwtService jwtService ,DoctorService doctorService , UserService userService,  VerificationTokenService verificationTokenService){
        this.doctorService =doctorService;
        this.jwtService = jwtService;
        this.userService = userService;

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
            UserResponseDTO userResponseDTO = null;
            if (dto.getRole().equals(Role.PATIENT)) {
                return userService.register(dto);
            } else if (dto.getRole().equals(Role.DOCTOR)) {
                userResponseDTO = doctorService.saveDoctor(dto);
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userResponseDTO);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GlobalExceptionHandler.internalError(new InternalException(e.getLocalizedMessage())));
        }}

//        catch (Exception e) {
//            e.printStackTrace();
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(e.getMessage());
//        }}


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
                       .body(GlobalExceptionHandler.internalError(new InternalException("Server is not accepting response try again after some time")));
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

}