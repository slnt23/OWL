package xyz.nanian.owl.common.mail;


import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 通用邮件消息，支持发件人显示名称、回复地址、HTML 正文、抄送和密送。
 *
 * @author slnt23
 * @since 2026/8/15
 */
@Data
@Builder
public class MailMessage {

    /**
     * 收件人邮箱
     */
    private String to;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件正文，html 为 true 时按 HTML 发送
     */
    private String body;

    /**
     * 发件人显示名称，为空时使用 spring.mail.sender-name
     */
    private String senderName;

    /**
     * 回复地址，可为空
     */
    private String replyTo;

    /**
     * 是否按 HTML 发送正文
     */
    private boolean html;

    /**
     * 抄送邮箱，可为空
     */
    private List<String> cc;

    /**
     * 密送邮箱，可为空
     */
    private List<String> bcc;
}
