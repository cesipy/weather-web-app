package at.qe.skeleton.internal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class for sending emails.
 * This service provides a method for sending simple email messages using the configured JavaMailSender.
 *
 * @see Service
 */
@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Sends a simple email message to the specified recipient.
     *
     * @param to      The email address of the recipient.
     * @param subject The subject of the email.
     * @param body    The body of the email.
     */
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
