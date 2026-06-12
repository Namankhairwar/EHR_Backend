package com.clinic.patient.service;


import com.clinic.patient.models.*;
import com.clinic.patient.entities.User;
import com.clinic.patient.repositories.UserRepository;
import com.clinic.patient.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Krishana dubey
 */
@Slf4j
@Service
public class UserService {


    @Autowired
    private  UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder , UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dob(LocalDate.parse(dto.getDob(),DateTimeFormatter.ofPattern("MM-dd-yyyy")))
                .gender(dto.getGender())
                .phoneNo(dto.getPhoneNo())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .bloodGroup(dto.getBloodGroup())
                .relation_name(dto.getRelation_name())
                .relation_no(dto.getRelation_no())
                .build();
        log.info("user processing {}",user);
        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(dto.getFirstName());

        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
        user.setRole(dto.getRole());
       user.setLastName(dto.getLastName());

        return mapToResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponseDTO mapToResponse(User user) {

        return new UserResponseDTO(
                user.getEmail(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getRelation_name(),
                user.getRelation_no()
        );
    }

    /**
     *
     * @param loginRequest
     * @return
     *
     * @implNote
     *       Tries to find the user
     *              @return Empty if no user found
     *       check the request password with the actual password
     *              @return Unauthorized if found unequal
     *
     *      @return grant the access
     *      Exception : caused Internal_Server_Error
     */
    public ResponseEntity<AuthLoginResponse> login(LoginRequest loginRequest){    try {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthLoginResponse(new LoginResponse(false,"User not found",null),null));
        }

        User user = userOptional.get();
        // Check password
        if (!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthLoginResponse(new LoginResponse(false,"Wrong Password",null),null));
        }

        // Login successful
       UserResponseDTO userData = mapToResponse(user);

        return ResponseEntity.ok(
              new AuthLoginResponse(new LoginResponse(true,"Authenticated",userData),new TokenResponse(jwtService.generateNewToken(loginRequest.getEmail()),jwtService.generateRefreshToken(loginRequest.getEmail()))));


    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthLoginResponse(new LoginResponse(false, "Login failed: " + e.getMessage(),null),null));
    }}

}
