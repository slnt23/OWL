package xyz.nanian.owl.common.mail.impl;

import jakarta.annotation.Resource;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.common.mail.MailMessage;
import xyz.nanian.owl.common.mail.MailService;

import java.nio.charset.StandardCharsets;

/**
 * 基于 Spring JavaMailSender 的邮件发送实现。
 */
@Component
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Resource
    final JavaMailSender mailSender;

    /**
     * 发件人邮箱，通过 owl.mail.username 配置。
     */
    @Value("${owl.mail.username}")
    private String from;

    /**
     * 默认发件人显示名称，通过 owl.mail.sender-name 配置。
     */
    @Value("${owl.mail.sender-name}")
    private String defaultSenderName;

    /**
     * 使用 SimpleMailMessage 发送纯文本邮件。
     */
    @Override
    public void send(String to, String subject, String body) {
        send(MailMessage.builder()
                .to(to)
                .subject(subject)
                .body(body)
                .build());
    }

    /**
     * 使用 MimeMessageHelper 发送完整邮件，支持发件人名称、HTML、回复地址、抄送和密送。
     */
    @Override
    @SneakyThrows
    public void send(MailMessage message) {
        jakarta.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage, true, StandardCharsets.UTF_8.name());

        String senderName = message.getSenderName() == null || message.getSenderName().isBlank()
                ? defaultSenderName
                : message.getSenderName();
        helper.setFrom(new InternetAddress(from, senderName));
        helper.setTo(message.getTo());
        helper.setSubject(message.getSubject());
        helper.setText(message.getBody(), message.isHtml());

        if (message.getReplyTo() != null && !message.getReplyTo().isBlank()) {
            helper.setReplyTo(message.getReplyTo());
        }
        if (message.getCc() != null && !message.getCc().isEmpty()) {
            helper.setCc(message.getCc().toArray(new String[0]));
        }
        if (message.getBcc() != null && !message.getBcc().isEmpty()) {
            helper.setBcc(message.getBcc().toArray(new String[0]));
        }

        mailSender.send(mimeMessage);
    }
}