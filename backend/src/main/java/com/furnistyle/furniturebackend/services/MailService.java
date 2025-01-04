package com.furnistyle.furniturebackend.services;

import org.springframework.scheduling.annotation.Async;

public interface MailService {
    @Async
    void sendEmail(String to, String subject, String body);
}