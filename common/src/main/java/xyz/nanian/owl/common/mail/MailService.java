package xyz.nanian.owl.common.mail;

/**
 * 邮件发送接口，所有业务模块共用。
 */
public interface MailService {

    /**
     * 使用默认发件人发送纯文本邮件。
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param body    邮件正文
     */
    void send(String to, String subject, String body);

    /**
     * 发送完整邮件消息，支持发件人名称、HTML、回复地址、抄送和密送。
     *
     * @param message 邮件消息
     */
    void send(MailMessage message);
}
