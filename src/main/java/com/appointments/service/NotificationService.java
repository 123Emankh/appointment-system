package com.appointments.service;

import com.appointments.domain.NotificationMessage;
import com.appointments.domain.User;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private List<Observer> observers;
    private boolean testMode;
    private List<String> sentMessages;

    public NotificationService() {
        this.observers = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
        this.testMode = false;
        // تسجيل المراقبين الافتراضيين
        registerObserver(new EmailObserver());
        registerObserver(new SMSObserver());
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(User user, NotificationMessage message) {
        String messageText = message.getContent();
        for (Observer observer : observers) {
            observer.notify(user, messageText);
        }
        if (testMode) {
            sentMessages.add("send email to " + user.getName() + ": " + messageText);
        }
    }

    // اختياري للتوافق
    public void notifyObservers(User user, String message) {
        notifyObservers(user, new NotificationMessage(message));
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public List<String> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }
}