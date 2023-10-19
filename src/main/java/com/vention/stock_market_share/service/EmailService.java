package com.vention.stock_market_share.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromMail;

    public String sendMailWithLink(String to, String link) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromMail);
            message.setTo(to);
            message.setSubject("Registration Confirmation");
            message.setText("Click the following link to confirm your registration: " + link);
            mailSender.send(message);
            return "mail sent";
        } catch (Exception e) {
            log.error("An error occurred while sending the email", e);
            return "failed to send mail";
        }
    }


}
