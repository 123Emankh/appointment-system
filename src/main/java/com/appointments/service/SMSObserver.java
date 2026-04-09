package com.appointments.service;

import com.appointments.domain.User;

/**
 * Observer implementation that sends notifications via SMS.
 *
 * @author Eman
 * @version 1.0
 */
public class SMSObserver implements Observer {
    /**
     * Sends an SMS notification to the user's phone number if available.
     *
     * @param user    the user to notify (must have a phone number)
     * @param message the message content
     */
    @Override
    public void notify(User user, String message) {
        if (user != null && user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            sendSMS(user.getPhoneNumber(), message);
        }
    }

    /**
     * Simulates sending an SMS (prints to console).
     *
     * @param phone   the recipient phone number
     * @param message the message content
     */
    private void sendSMS(String phone, String message) {
        System.out.println("SMS sent to " + phone + ": " + message);
    }
    
    /**
     * Default constructor.
     */
    public SMSObserver() {
    }
}