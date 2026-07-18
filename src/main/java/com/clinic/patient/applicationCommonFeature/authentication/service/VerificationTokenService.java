package com.clinic.patient.applicationCommonFeature.authentication.service;

import com.clinic.patient.applicationCommonFeature.authentication.entity.VerificationToken;
import com.clinic.patient.applicationCommonFeature.authentication.repository.VerificationTokenRepository;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    public VerificationToken createVerificationToken(User user){

        // remove old token if exist
        verificationTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken =
                VerificationToken.builder()
                        .token(token)
                        .user(user)
                        .expiryDate(LocalDateTime.now().plusMinutes(15))
                        .build();

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
