package com.appointments.service;

import java.util.Properties;
import java.util.logging.Logger;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Service class for sending emails using the Jakarta Mail API.
 * Configures SMTP settings for Gmail (TLS, port 587) and trusts smtp.gmail.com.
 *
 * @author Eman
 * @version 2.0
 */
public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());
    private final String username;
    private final String password;

    /**
     * Constructs an EmailService with the given credentials.
     *
     * @param username the Gmail address (e.g., you@gmail.com)
     * @param password the App Password (not your regular password)
     */
    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Sends an email to the specified recipient.
     *
     * @param to      recipient email address
     * @param subject email subject
     * @param body    email body text
     * @throws RuntimeException if sending fails
     */
    public void sendEmail(String to, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); 

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            LOGGER.info(" Email sent successfully to " + to);
        } catch (MessagingException e) {
            LOGGER.severe("Failed to send email to " + to + ": " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}