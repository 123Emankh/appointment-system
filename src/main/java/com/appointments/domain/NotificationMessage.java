
package com.appointments.domain;

import java.time.LocalDateTime;

/**
 * Value Object for notification messages
 * 
 * @author Team 3
 * @version 1.0
 */
public class NotificationMessage {
    private String content;
    private LocalDateTime timestamp;
    private String type;

    /**
     * Constructor with message content
     * 
     * @param content the message content
     */
    public NotificationMessage(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.type = "GENERAL";
    }

    /**
     * Constructor with all fields
     * 
     * @param content the message content
     * @param type the message type
     */
    public NotificationMessage(String content, String type) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.type = type;
    }

    // Getters
    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    // Setters (if needed)
    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp, type, content);
    }
}