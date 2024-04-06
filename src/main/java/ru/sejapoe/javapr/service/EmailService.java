package ru.sejapoe.javapr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${email.to}")
    private String to;

    @Async
    public void send(String to, String title, String message) {
        log.info("Faking sending mail to {}:\nRe: {}\n\n{}", to, title, message);
    }

    public void sendAdmin(String title, String message) {
        send(this.to, title, message);
    }
}
