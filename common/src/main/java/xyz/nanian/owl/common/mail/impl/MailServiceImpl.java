package xyz.nanian.owl.common.mail.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.common.mail.MailService;

/**
 * Simple mail transport backed by Spring JavaMailSender.
 */
@Component
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    /**
     * [UPGRADE] sender is configurable via spring.mail.username.
     */
    @Value("${spring.mail.username:3436134614@qq.com}")
    private String from;

    @Override
    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
