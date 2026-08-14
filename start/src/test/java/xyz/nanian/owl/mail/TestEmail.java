package xyz.nanian.owl.mail;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 测试邮箱
 *
 * @author slnt23
 * @since 2026/4/10
 */

public class TestEmail {

    @Resource
    private JavaMailSender mailSender;

    @PostConstruct
    public void test() {
        System.out.println(mailSender);
    }
}
