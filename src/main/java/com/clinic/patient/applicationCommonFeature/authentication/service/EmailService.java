package com.clinic.patient.applicationCommonFeature.authentication.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail(String toEmail, String firstName, String token) {

        String verificationUrl =
                "http://localhost:8086/api/auth/verify?token=" + token;

        try {

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("vans.healthcare.ehr@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Verify Your Email Address - VANS Digital Healthcare");

            String html = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Verify Your Email</title>
                        <!--[if mso]>
                        <noscript>
                            <xml>
                                <o:OfficeDocumentSettings>
                                    <o:PixelsPerInch>96</o:PixelsPerInch>
                                </o:OfficeDocumentSettings>
                            </xml>
                        </noscript>
                        <![endif]-->
                    </head>

                    <body style="margin:0;padding:0;background-color:#F8FAFC;font-family:'Inter',Arial,Helvetica,sans-serif;-webkit-font-smoothing:antialiased;">

                    <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0" style="background-color:#F8FAFC;">
                        <tr>
                            <td align="center" style="padding:56px 20px;">

                                <table role="presentation" width="560" cellpadding="0" cellspacing="0" border="0" style="max-width:560px;width:100%%;">

                                    <!-- Brand Logo -->
                                    <tr>
                                        <td align="center" style="padding-bottom:24px;">
                                            <img src="cid:logoImage" alt="VANS Digital Healthcare" width="280" style="display:block;width:280px;max-width:80%%;height:auto;border:0;outline:none;text-decoration:none;">
                                        </td>
                                    </tr>

                    

                                    <!-- Divider -->
                                    <tr>
                                        <td style="border-top:1px solid #E5E7EB;padding-top:40px;"></td>
                                    </tr>

                                    <!-- Main Heading -->
                                    <tr>
                                        <td align="center" style="padding-bottom:24px;">
                                            <h1 style="margin:0;font-family:'Inter',Arial,sans-serif;font-size:26px;font-weight:700;color:#1F2937;line-height:1.4;">
                                                Verify your email address
                                            </h1>
                                        </td>
                                    </tr>

                                    <!-- Body Copy -->
                                    <tr>
                                        <td style="padding-bottom:8px;">
                                            <p style="margin:0 0 20px 0;font-family:'Inter',Arial,sans-serif;font-size:16px;color:#4B5563;line-height:1.8;">
                                                Hello <strong style="color:#1F2937;">%s</strong>,
                                            </p>
                                            <p style="margin:0 0 20px 0;font-family:'Inter',Arial,sans-serif;font-size:16px;color:#4B5563;line-height:1.8;">
                                                Welcome to <strong style="color:#1F2937;">VANS Digital Healthcare</strong>. Thank you for creating your account.
                                            </p>
                                            <p style="margin:0;font-family:'Inter',Arial,sans-serif;font-size:16px;color:#4B5563;line-height:1.8;">
                                                To activate your account and keep it secure, please verify your email address by clicking the button below.
                                            </p>
                                        </td>
                                    </tr>

                                    <!-- CTA Button -->
                                    <tr>
                                        <td align="center" style="padding:36px 0;">
                                            <table role="presentation" cellpadding="0" cellspacing="0" border="0">
                                                <tr>
                                                    <td align="center" bgcolor="#2563EB" style="border-radius:10px;">
                                                        <a href="%s"
                                                           target="_blank"
                                                           style="display:inline-block;padding:15px 40px;font-family:'Inter',Arial,sans-serif;font-size:16px;font-weight:600;color:#FFFFFF;text-decoration:none;border-radius:10px;background-color:#2563EB;">
                                                            Verify Email
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>

                                    <!-- Expiry / Notes -->
                                    <tr>
                                        <td style="padding-bottom:6px;">
                                            <p style="margin:0 0 14px 0;font-family:'Inter',Arial,sans-serif;font-size:14px;color:#6B7280;line-height:1.8;">
                                                ⏰ This verification link will expire in <strong style="color:#4B5563;">15 minutes</strong>.
                                            </p>
                                            <p style="margin:0;font-family:'Inter',Arial,sans-serif;font-size:14px;color:#6B7280;line-height:1.8;">
                                                If you didn't create this account, you can safely ignore this email.
                                            </p>
                                        </td>
                                    </tr>

                                    <!-- Signature -->
                                    <tr>
                                        <td style="padding-top:36px;">
                                            <p style="margin:0;font-family:'Inter',Arial,sans-serif;font-size:15px;color:#4B5563;line-height:1.8;">
                                                Regards,<br>
                                                <strong style="color:#1F2937;">VANS Digital Healthcare Team</strong>
                                            </p>
                                        </td>
                                    </tr>

                                    <!-- Divider -->
                                    <tr>
                                        <td style="border-top:1px solid #E5E7EB;padding-top:32px;margin-top:40px;"></td>
                                    </tr>

                                    <!-- Footer -->
                                    <tr>
                                        <td align="center" style="padding-top:24px;">
                                            <p style="margin:0;font-family:'Inter',Arial,sans-serif;font-size:12px;color:#9CA3AF;line-height:1.8;">
                                                © 2026 VANS Digital Healthcare. All rights reserved.
                                            </p>
                                        </td>
                                    </tr>

                                </table>

                            </td>
                        </tr>
                    </table>

                    </body>
                    </html>
                    """.formatted(firstName, verificationUrl);

            helper.setText(html, true);

            ClassPathResource logo = new ClassPathResource("static/images/vans-logo.jpeg");
            helper.addInline("logoImage", logo);

            javaMailSender.send(message);

            log.info("Verification email sent to {}", toEmail);

        } catch (Exception e) {
            log.error("Email sending failed", e);
        }
    }
}