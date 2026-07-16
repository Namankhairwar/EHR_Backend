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


    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthLoginResponse(new LoginResponse(false, "User not found", null), null));
        }

        User user = userOptional.get();

        // Email verification check
        if (!user.isEmailVerified()) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
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


        // Check password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthLoginResponse(new LoginResponse(false, "Wrong Password", null), null));
        }

        // Login successful
        UserResponseDTO userData = MAP.map(user, UserResponseDTO::new);

        TokenResponse tokenResponse =
                new TokenResponse(
                        jwtService.generateNewToken(user.getEmail()),
                        jwtService.generateRefreshToken(user.getEmail())
                );

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
        log.info(dto.toString());
        User user = MAP.map(dto, User::new);
        log.info(user.toString());

        // first save usser
        User savedUser = userRepository.save(user);

        // create verification token
        VerificationToken verificationToken = verificationTokenService.createVerificationToken(savedUser);


        // send verification email
        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                savedUser.getFirstName(),
                verificationToken.getToken()
        );

        UserResponseDTO responseDTO =
                MAP.map(savedUser, UserResponseDTO::new);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        "Registration successful. Please check your email and verify your account."
                );


    }

}