package com.appointments.service;

import com.appointments.domain.User;

public class EmailObserver implements Observer {
    @Override
    public void notify(User user, String message) {
        sendEmail(user.getEmail(), message);
    }

    private void sendEmail(String email, String message) {
        System.out.println("📧 Email sent to " + email + ": " + message);
    }
}