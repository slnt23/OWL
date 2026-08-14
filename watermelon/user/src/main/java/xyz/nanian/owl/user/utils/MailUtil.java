package xyz.nanian.owl.user.utils;

import org.springframework.stereotype.Component;
import xyz.nanian.owl.common.mail.MailService;

/**
 * [TO_BE_DELETED] 邮件发送已迁移至 common MailService，本类仅保留旧调用入口。
 */
@Deprecated
@Component
public class MailUtil {

    private final MailService mailService;

    public MailUtil(MailService mailService) {
        this.mailService = mailService;
    }

    public void sendMail(String to, String subject, String body) {
        mailService.send(to, subject, body);
    }
}
