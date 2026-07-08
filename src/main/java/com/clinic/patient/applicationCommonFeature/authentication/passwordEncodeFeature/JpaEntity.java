package com.clinic.patient.applicationCommonFeature.authentication.passwordEncodeFeature;


import com.clinic.patient.user.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Krishana dubey
 */

@Slf4j
@Configuration
public class JpaEntity {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JpaEntity(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init(){
        User.setPasswordEncoder(passwordEncoder);
        log.info("Successfully passwordEncoder is injected to user entity");
    }
}
