package com.appointments.service;

import com.appointments.domain.NotificationMessage;
import com.appointments.domain.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing and notifying observers.
 * Implements the Observer pattern to notify multiple channels such as email and SMS.
 */
public class NotificationService {
    private List<Observer> observers;
    private boolean testMode;
    private List<String> sentMessages;

    public NotificationService() {
        this.observers = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
        this.testMode = false;
    }

    public NotificationService(EmailService emailService) {
        this();
        registerObserver(new EmailObserver(emailService));
        registerObserver(new SMSObserver());
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers with a NotificationMessage object.
     */
    public void notifyObservers(User user, NotificationMessage message) {
    	 if (user == null) {
    	        System.err.println("Cannot notify: User is null");
    	        return;
    	    }
    	    
    	    if (message == null) {
    	        System.err.println("Cannot notify: Message is null");
    	        return;
    	    }
    	    
    	for (Observer observer : observers) {
            if (observer instanceof EmailObserver && testMode) {
                continue;
            }
            observer.notify(user, message.getContent());
        }

        if (testMode) {
            sentMessages.add("send email to " + user.getName() + ": " + message.getContent());
        }
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public List<String> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }
}