package com.clinic.patient.user.service;


import com.clinic.patient.applicationCommonFeature.authentication.dto.auth.AuthLoginResponse;
import com.clinic.patient.applicationCommonFeature.authentication.dto.auth.AuthResponse;
import com.clinic.patient.applicationCommonFeature.authentication.dto.jwt.TokenResponse;
import com.clinic.patient.applicationCommonFeature.authentication.dto.login.LoginRequest;
import com.clinic.patient.applicationCommonFeature.authentication.dto.login.LoginResponse;
import com.clinic.patient.applicationCommonFeature.authentication.entity.VerificationToken;
import com.clinic.patient.applicationCommonFeature.authentication.service.EmailService;
import com.clinic.patient.applicationCommonFeature.authentication.service.VerificationTokenService;
import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.user.dto.UserRequestDTO;
import com.clinic.patient.user.dto.UserResponseDTO;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import com.clinic.patient.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.AlreadyBuiltException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Krishana dubey
 *
 * @getAllUsers
 * @getUserById
 * @updateToUser - Profile ,Sensitive - *(Blood)
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    // for Email verification
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;


    public List<UserResponseDTO> getAllUsers() {

       return userRepository.findAll()
                .stream()
                .map(t->MAP.map(t,UserResponseDTO::new))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T> T getUserById(Long id , Class<T> returnType)throws GlobalExceptionHandler {
        if(returnType== UserResponseDTO.class)
            return (T)userRepository.findById(id)
                    .map(t->MAP.map(t,UserResponseDTO::new))
                    .orElseThrow(() -> new RuntimeException("User not found"));

        else if(returnType == User.class)
            return (T) userRepository.findById(id)
                    .orElseThrow(()->new RuntimeException("User not found"));
        throw new GlobalExceptionHandler("Class is not defined in getUserById in UserService");
    }



    public UserResponseDTO updateUser(Long id, UserRequestDTO dto)throws GlobalExceptionHandler{
        User user = getUserById(id,User.class);
        MAP.copyInTheObject(dto,user);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
      return  MAP.map(user,UserResponseDTO::new);
    }



    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    /**
     * @param loginRequest
     * @return
     * @return Empty if no user found
     * check the request password with the actual password
     * @return Unauthorized if found unequal
     * @return grant the access
     * Exception : caused Internal_Server_Error
     * @implNote Tries to find the user
     */

    public ResponseEntity<?> login(LoginRequest loginRequest) {

        log.info("LOGIN START");
        // first: Find user by email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        log.info("USER FOUND : {}", userOptional.isPresent());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new AuthLoginResponse(
                                    new LoginResponse(false, "User not found", null),
                                    null
                            )
                    );
        }

        User user = userOptional.get();

        //----------

        String rawPassword = loginRequest.getPassword();
        String encodedPassword = user.getPassword();

        log.info("LOGIN PASSWORD : {}", rawPassword);
        log.info("DB PASSWORD : {}", encodedPassword);

        boolean isMatch = passwordEncoder.matches(
                rawPassword,
                encodedPassword
        );

        log.info("PASSWORD MATCH RESULT : {}", isMatch);

        if (!isMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new AuthLoginResponse(
                                    new LoginResponse(false, "Wrong Password", null),
                                    null
                            )
                    );
        }
        //---------------------

        log.info("EMAIL VERIFIED : {}", user.isEmailVerified());

        //3rd: Check email verification
        if (!user.isEmailVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(
                            new AuthLoginResponse(
                                    new LoginResponse(
                                            false,
                                            "Please verify your email first",
                                            null
                                    ),
                                    null
                            )
                    );
        }

        log.info("Generating JWT");

        //4th: Generate JWT tokens
        TokenResponse tokenResponse = new TokenResponse(
                jwtService.generateNewToken(user.getEmail()),
                jwtService.generateRefreshToken(user.getEmail())
        );

        log.info("JWT Generated Successfully");

        //5th: User response
        UserResponseDTO userData = MAP.map(user, UserResponseDTO::new);

        return ResponseEntity.ok(
                new AuthLoginResponse(
                        new LoginResponse(
                                true,
                                "Authenticated",
                                userData
                        ),
                        tokenResponse
                )
        );
    }

    public boolean doesUserExist(long id){
        Optional<User> byId = userRepository.findById(id);
        return byId.isPresent();
    }

    public ResponseEntity<?> register(UserRequestDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.ALREADY_REPORTED)
                    .body(GlobalExceptionHandler.internalError(new AlreadyBuiltException("Already Exist account")));
        }

        log.info("Step 1 - Email checked");

        log.info(dto.toString());
        User user = MAP.map(dto, User::new);

        log.info("Step 2 - User mapped");

        // Encode password before saving
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        // Email should be unverified by default
        user.setEmailVerified(false);

        log.info(user.toString());

        // Save user
        User savedUser = userRepository.save(user);
        log.info("Step 3 - User saved");

        // Create verification token
        VerificationToken verificationToken =
                verificationTokenService.createVerificationToken(savedUser);


        log.info("Token created: {}", verificationToken.getToken());
        // Send verification email
        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                savedUser.getFirstName(),
                verificationToken.getToken()
        );

        log.info("Email sent");

        // Return success response
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Registration successful. Please check your email and verify your account.");
    }

}