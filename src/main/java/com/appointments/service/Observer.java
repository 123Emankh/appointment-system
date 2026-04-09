package com.appointments.service;

import com.appointments.domain.User;

/**
 * Observer interface for the notification system.
 * Any class that wants to receive notifications (e.g., email, SMS) must implement this interface.
 *
 * @author Eman 
 * @version 1.0
 */
public interface Observer {
    /**
     * Notifies the observer with a message for a specific user.
     *
     * @param user    the user to notify
     * @param message the notification content
     */
    void notify(User user, String message);
}