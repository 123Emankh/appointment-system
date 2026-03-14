package com.appointments.service;

import com.appointments.domain.User;

public class SMSObserver implements Observer {
    @Override
    public void notify(User user, String message) {
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            sendSMS(user.getPhoneNumber(), message);
        }
    }

    private void sendSMS(String phone, String message) {
        System.out.println("📱 SMS sent to " + phone + ": " + message);
    }
}