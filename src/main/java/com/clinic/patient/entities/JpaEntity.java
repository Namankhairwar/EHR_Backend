package com.clinic.patient.entities;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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
