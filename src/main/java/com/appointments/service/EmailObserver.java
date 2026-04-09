package com.appointments.service;
/**
 * Observer that sends email notifications to users.
 * Uses the EmailService to actually send the messages.
 *
 * @author Eman 
 * @version 1.0
 */
import com.appointments.domain.User;
public class EmailObserver implements Observer {
    private final EmailService emailService;

    public EmailObserver(EmailService emailService) {  // <-- أضف هذا المنشئ
        this.emailService = emailService;
    }

    @Override
    public void notify(User user, String message) {
        if (user != null && user.getEmail() != null) {
            emailService.sendEmail(user.getEmail(), "Appointment Reminder", message);
        }
    }
}
    
