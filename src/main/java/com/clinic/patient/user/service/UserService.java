package com.clinic.patient.user.service;

import com.clinic.patient.applicationCommonFeature.authentication.dto.auth.AuthLoginResponse;
import com.clinic.patient.applicationCommonFeature.authentication.dto.jwt.TokenResponse;
import com.clinic.patient.applicationCommonFeature.authentication.dto.login.LoginRequest;
import com.clinic.patient.applicationCommonFeature.authentication.dto.login.LoginResponse;
import com.clinic.patient.applicationCommonFeature.authentication.entity.VerificationToken;
import com.clinic.patient.applicationCommonFeature.authentication.service.EmailService;
import com.clinic.patient.applicationCommonFeature.authentication.service.VerificationTokenService;
import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.applicationCommonFeature.state.Role;
import com.clinic.patient.notification.service.NotificationService;
import com.clinic.patient.security.jwt.JwtService;
import com.clinic.patient.user.dto.RestrictionGrantResponseDto;
import com.clinic.patient.user.dto.RestrictionRequestDto;
import com.clinic.patient.user.dto.UserRequestDTO;
import com.clinic.patient.user.dto.UserResponseDTO;
import com.clinic.patient.user.entity.RestrictionGrant;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.RestrictionGrantRepository;
import com.clinic.patient.user.repositories.UserRepository;
import com.clinic.patient.user.state.RestrictionGrantStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.AlreadyBuiltException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {


   private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RestrictionGrantRepository restrictionGrantRepository;
    private final NotificationService notificationService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

     public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RestrictionGrantRepository restrictionGrantRepository,
                       NotificationService notificationService,
                       VerificationTokenService verificationTokenService,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.restrictionGrantRepository = restrictionGrantRepository;
        this.notificationService = notificationService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        String email = (String) authentication.getPrincipal();
        return userRepository.findByEmail(email).orElse(null);
    }

    public void filterUserResponse(UserResponseDTO dto) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getRole() == Role.ADMIN ||
                currentUser.getEhrId().equals(dto.getEhrId())) {
                return;
            }
            
            Optional<RestrictionGrant> grantOpt = restrictionGrantRepository
                    .findByPatient_EhrIdAndAccessor_EhrId(dto.getEhrId(), currentUser.getEhrId());
            
            if (grantOpt.isEmpty() || grantOpt.get().getStatus() != RestrictionGrantStatus.APPROVED) {
                dto.setPhoneNo(null);
                dto.setDob(null);
                dto.setBloodGroup(null);
                dto.setAddress(null);
                dto.setEmergencyContact(null);
                dto.setMaritalStatus(null);
            } else {
                RestrictionGrant grant = grantOpt.get();
                String restrictedStr = grant.getRestrictedAttributes();
                List<String> allowed = new ArrayList<>();
                if (restrictedStr != null && !restrictedStr.isEmpty()) {
                    for (String s : restrictedStr.split(",")) {
                        allowed.add(s.trim().toLowerCase());
                    }
                }
                
                if (!allowed.contains("phoneno")) dto.setPhoneNo(null);
                if (!allowed.contains("dob")) dto.setDob(null);
                if (!allowed.contains("bloodgroup")) dto.setBloodGroup(null);
                if (!allowed.contains("address")) dto.setAddress(null);
                if (!allowed.contains("emergencycontact")) dto.setEmergencyContact(null);
                if (!allowed.contains("maritalstatus")) dto.setMaritalStatus(null);
            }
        }
    }

    public List<UserResponseDTO> searchPatients(String query) {
        return userRepository.searchPatients(query).stream()
                .map(t -> MAP.map(t, UserResponseDTO::new))
                .peek(this::filterUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void requestRestrictionGrant(RestrictionRequestDto dto) {
        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        User accessor = userRepository.findById(dto.getAccessorId())
                .orElseThrow(() -> new RuntimeException("Accessor not found"));

        Optional<RestrictionGrant> existing = restrictionGrantRepository
                .findByPatient_EhrIdAndAccessor_EhrId(dto.getPatientId(), dto.getAccessorId());

        String attrs = "";
        if (dto.getRestrictedAttributes() != null) {
            attrs = String.join(",", dto.getRestrictedAttributes());
        }

        RestrictionGrant grant;
        if (existing.isPresent()) {
            grant = existing.get();
            grant.setStatus(RestrictionGrantStatus.PENDING);
            grant.setRestrictedAttributes(attrs);
            grant.setRequestedAt(LocalDateTime.now());
        } else {
            grant = RestrictionGrant.builder()
                    .patient(patient)
                    .accessor(accessor)
                    .status(RestrictionGrantStatus.PENDING)
                    .restrictedAttributes(attrs)
                    .requestedAt(LocalDateTime.now())
                    .build();
        }
        restrictionGrantRepository.save(grant);

        String accessorName = accessor.getFirstName() + " " + accessor.getLastName();
        String message = accessorName + " has requested access to some of your restricted profile attributes.";
        notificationService.createNotification(patient, message);
    }

    private void checkGrantOwnership(RestrictionGrant grant) {
        User currentUser = getCurrentUser();
        if (currentUser == null || (currentUser.getRole() != Role.ADMIN
                && !grant.getPatient().getEhrId().equals(currentUser.getEhrId()))) {
            throw new RuntimeException("You can only respond to your own permission requests");
        }
    }

    @Transactional
    public void approveRestrictionGrant(Long requestId, List<String> attributes) {
        RestrictionGrant grant = restrictionGrantRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Restriction grant request not found"));
        checkGrantOwnership(grant);
        grant.setStatus(RestrictionGrantStatus.APPROVED);
        if (attributes != null) {
            grant.setRestrictedAttributes(String.join(",", attributes));
        }
        grant.setRespondedAt(LocalDateTime.now());
        restrictionGrantRepository.save(grant);

        String patientName = grant.getPatient().getFirstName() + " " + grant.getPatient().getLastName();
        String message = "Patient " + patientName + " has approved your request to access their profile attributes.";
        notificationService.createNotification(grant.getAccessor(), message);
    }

    @Transactional
    public void rejectRestrictionGrant(Long requestId) {
        RestrictionGrant grant = restrictionGrantRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Restriction grant request not found"));
        checkGrantOwnership(grant);
        grant.setStatus(RestrictionGrantStatus.REJECTED);
        grant.setRespondedAt(LocalDateTime.now());
        restrictionGrantRepository.save(grant);

        String patientName = grant.getPatient().getFirstName() + " " + grant.getPatient().getLastName();
        String message = "Patient " + patientName + " has rejected your request to access their profile attributes.";
        notificationService.createNotification(grant.getAccessor(), message);
    }

    public List<RestrictionGrantResponseDto> getRestrictionGrantsForPatient(String patientId) {
        return restrictionGrantRepository.findAllByPatient_EhrId(patientId).stream()
                .map(this::mapGrantToDto)
                .collect(Collectors.toList());
    }

    private RestrictionGrantResponseDto mapGrantToDto(RestrictionGrant grant) {
        RestrictionGrantResponseDto dto = new RestrictionGrantResponseDto();
        dto.setId(grant.getId());
        dto.setPatientId(grant.getPatient().getEhrId());
        dto.setPatientName(grant.getPatient().getFirstName() + " " + grant.getPatient().getLastName());
        dto.setAccessorId(grant.getAccessor().getEhrId());
        dto.setAccessorName(grant.getAccessor().getFirstName() + " " + grant.getAccessor().getLastName());
        dto.setStatus(grant.getStatus());
        
        List<String> attrsList = new ArrayList<>();
        if (grant.getRestrictedAttributes() != null && !grant.getRestrictedAttributes().isEmpty()) {
            for (String s : grant.getRestrictedAttributes().split(",")) {
                attrsList.add(s.trim());
            }
        }
        dto.setRestrictedAttributes(attrsList);
        dto.setRequestedAt(grant.getRequestedAt());
        dto.setRespondedAt(grant.getRespondedAt());
        return dto;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(t -> MAP.map(t, UserResponseDTO::new))
                .peek(this::filterUserResponse)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T> T getUserById(String id, Class<T> returnType) throws GlobalExceptionHandler {
        if (returnType == UserResponseDTO.class) {
            UserResponseDTO dto = userRepository.findById(id)
                    .map(t -> MAP.map(t, UserResponseDTO::new))
                    .orElseThrow(() -> new RuntimeException("User not found"));
            filterUserResponse(dto);
            return (T) dto;
        } else if (returnType == User.class) {
            return (T) userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new GlobalExceptionHandler("Class is not defined in getUserById in UserService");
    }

    public UserResponseDTO updateUser(String id, UserRequestDTO dto) throws GlobalExceptionHandler {
        User user = getUserById(id, User.class);
        Role originalRole = user.getRole();
        MAP.copyInTheObject(dto, user, "role");
        user.setRole(originalRole);
        return MAP.map(userRepository.save(user), UserResponseDTO::new);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public boolean doesUserExist(String id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.isPresent();
    }

    // -------------------- NEW RESEND VERIFICATION METHOD --------------------
    @Transactional
    public VerificationToken resendVerificationEmail(User user) {
        // Remove any existing token for this user to prevent duplicates
        VerificationToken token = verificationTokenService.createVerificationToken(user);
        emailService.sendVerificationEmail(
                user.getEmail(),
                user.getFirstName(),
                token.getToken()
        );
        return token;
    }

    // -------------------- MODIFIED LOGIN METHOD --------------------
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        log.info("LOGIN START");

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        log.info("USER FOUND : {}", userOptional.isPresent());

        if (userOptional.isEmpty()) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthLoginResponse(
                            new LoginResponse(false, "User not found", null),
                            null
                    ));
        }

        User user = userOptional.get();

        String rawPassword = loginRequest.getPassword();
        String encodedPassword = user.getPassword();
        log.info("LOGIN PASSWORD : {}", rawPassword);
        log.info("DB PASSWORD : {}", encodedPassword);

        boolean isMatch = passwordEncoder.matches(rawPassword, encodedPassword);
        log.info("PASSWORD MATCH RESULT : {}", isMatch);

        if (!isMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthLoginResponse(
                            new LoginResponse(false, "Wrong Password", null),
                            null
                    ));
        }

        // If email not verified, resend a new verification link and return it
        if (!user.isEmailVerified()) {
            resend(loginRequest.getEmail());
       
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AuthLoginResponse(
                            new LoginResponse(
                                  false,
                                    "Email not verified. A new verification link has been sent to your email.",
                                    null
                            ),
                            null
                    ));
        
        }
        log.info("Generating JWT");
        TokenResponse tokenResponse = new TokenResponse(
                jwtService.generateNewToken(user.getEmail()),
                jwtService.generateRefreshToken(user.getEmail())
        );
        log.info("JWT Generated Successfully");

        UserResponseDTO userData = MAP.map(user, UserResponseDTO::new);
        return ResponseEntity.ok(
                new AuthLoginResponse(
                        new LoginResponse(true, "Authenticated", userData),
                        tokenResponse
                )
        );
    }


    // -------------------- REGISTER METHOD (unchanged) --------------------
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

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmailVerified(false);
        log.info(user.toString());

        User savedUser = userRepository.save(user);
        log.info("Step 3 - User saved");

        VerificationToken verificationToken = verificationTokenService.createVerificationToken(savedUser);
        String verificationLink = "https://ehrbackend-production-de58.up.railway.app/api/auth/verify?token=" + verificationToken.getToken();
        log.info("Token created: {}", verificationToken.getToken());
        log.info("Verification link: {}", verificationLink);

        try {
            emailService.sendVerificationEmail(
                    savedUser.getEmail(),
                    savedUser.getFirstName(),
                    verificationToken.getToken()
            );
            log.info("Email sent successfully");
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Registration successful. Please check your email and verify your account.");
        } catch (Exception e) {
            log.error("Failed to send verification email: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Registration successful. Note: Email verification service is currently unreachable. Please use this verification link manually: " + verificationLink);
        }
    }
    public ResponseEntity<String> resend(String userR){
         try {
            User user=userRepository.findByEmail(userR).get();
        VerificationToken verificationToken = verificationTokenService.resendVerificationToken(user);
        String verificationLink = "https://ehrbackend-production-de58.up.railway.app/api/auth/verify?token=" + verificationToken.getToken();
        log.info("Token created: {}", verificationToken.getToken());
        log.info("Verification link: {}", verificationLink);

       
            emailService.sendVerificationEmail(
                   user.getEmail(),
                   user.getFirstName(),
                    verificationToken.getToken()
            );
            log.info("Email sent successfully");
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Registration successful. Please check your email and verify your account.");
        } catch (Exception e) {
            log.error("Failed to send verification email: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Registration successful. Note: Email verification service is currently unreachable. Please use this verification link manually: " );
        }
    
    }
}