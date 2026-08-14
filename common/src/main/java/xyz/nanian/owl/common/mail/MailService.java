package xyz.nanian.owl.common.mail;

/**
 * Shared mail sending contract for all business modules.
 */
public interface MailService {

    void send(String to, String subject, String body);
}
