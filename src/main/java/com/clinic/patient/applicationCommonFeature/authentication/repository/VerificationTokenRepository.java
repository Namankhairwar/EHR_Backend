package com.clinic.patient.applicationCommonFeature.authentication.repository;

import com.clinic.patient.applicationCommonFeature.authentication.entity.VerificationToken;
import com.clinic.patient.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUser_EhrId(String id);
    void deleteByUser(User user);
    void deleteByUser_EhrId(String id);
    // delete when verification done
}
