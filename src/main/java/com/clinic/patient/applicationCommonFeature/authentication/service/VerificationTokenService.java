package com.clinic.patient.applicationCommonFeature.authentication.service;

import com.clinic.patient.applicationCommonFeature.authentication.entity.VerificationToken;
import com.clinic.patient.applicationCommonFeature.authentication.repository.VerificationTokenRepository;
import com.clinic.patient.user.dto.UserRequestDTO;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    public VerificationToken resendVerificationToken(User user) throws Exception {
        if (user.getEhrId() == null) {
            throw new Exception("User EHR ID cannot be null");
        }
        VerificationToken verificationToken = verificationTokenRepository.findByUser_EhrId(user.getEhrId())
                .orElseGet(() -> VerificationToken.builder().user(user).build());

        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        return verificationTokenRepository.save(verificationToken);
    }
    
    public VerificationToken createVerificationToken(User user) {
        if (user.getEhrId() == null) {
            throw new RuntimeException("User EHR ID cannot be null");
        }
        VerificationToken verificationToken = verificationTokenRepository.findByUser_EhrId(user.getEhrId())
                .orElseGet(() -> VerificationToken.builder().user(user).build());

        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        return verificationTokenRepository.save(verificationToken);
    }
    

    
    public User verifyToken(String token){

        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token)
                        .orElseThrow(() -> new RuntimeException("Invalid verification token"));


        // checking expiry
        if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now())){

            verificationTokenRepository.delete(verificationToken);

            throw new RuntimeException("Verification token expired");
        }

        User user = verificationToken.getUser();

        user.setEmailVerified(true);

        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);

        return user;
    }
}
