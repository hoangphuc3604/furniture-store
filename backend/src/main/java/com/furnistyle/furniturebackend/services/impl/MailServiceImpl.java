package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String mailServer;

    private final JavaMailSender javaMailSender;

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(mailServer);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi xảy ra trong quá trình gửi mail!", e);
        }
    }
}