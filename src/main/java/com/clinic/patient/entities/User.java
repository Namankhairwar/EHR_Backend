package com.clinic.patient.entities;

import com.clinic.patient.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Transient
    private static PasswordEncoder passwordEncoder;

    @PrePersist
    @PreUpdate
    private void encode(){
        password= passwordEncoder.encode(this.password);
      log.info("Password : {} and encoded pass {}",password,passwordEncoder);
    }

    public static void setPasswordEncoder(PasswordEncoder passwordEncode){
       User.passwordEncoder = passwordEncode;
    }
}
