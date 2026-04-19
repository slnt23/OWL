package xyz.nanian.owl.utils.mail;


import jakarta.annotation.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Component;
import xyz.nanian.owl.constant.MailConstant;

/**
 * 邮箱登录，注册
 *
 * @author slnt23
 * @since 2026/4/9
 */

@Component
public class MailUtil {

//    @Autowired
    @Resource
    public JavaMailSender mailSender;

//    构造注入
//    public Mail(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

    /**
     * 验证码发送,
     * 这里应该可以复用，
     * @param to
     * @param subject
     * @param body
     */
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

//        发件邮箱
        message.setFrom(MailConstant.MAIL);
//        收件人邮箱
        message.setTo(to);
//        邮箱标题
        message.setSubject(subject);
//        邮件正文内容
        message.setText(body);

        mailSender.send(message);
    }
}
