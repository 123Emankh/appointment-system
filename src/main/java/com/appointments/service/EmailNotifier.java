package com.appointments.service;

import com.appointments.domain.User;

/**
 * Observer that sends notifications via email.
 *
 * @author Eman
 * @version 1.0
 */
public class EmailNotifier implements Observer {
    private final EmailService emailService;

    public EmailNotifier(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notify(User user, String message) {
        if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
            emailService.sendEmail(user.getEmail(), "Appointment Reminder", message);
        }
    }
}