package com.clinic.patient.applicationCommonFeature.authentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail(String toEmail, String firstName, String token){

        String verificationUrl = "http://localhost:8086/api/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("rekandlal11@gmail.com");

        message.setTo(toEmail);

        message.setSubject("Verify your EHR account");

        message.setText(
                "Hello " + firstName + ",\n\n"
                        + "Welcome to EHR Platform.\n\n"
                        + "Please verify your email by clicking the link below:\n\n"
                        + verificationUrl
                        + "\n\n"
                        + "This link will expire in 15 minutes.\n\n"
                        + "Thank you,\n"
                        + "EHR Team"
        );



        try {
            javaMailSender.send(message);
        }
        catch(Exception e){
            log.error("Email sending failed", e);
        }

    }

}
