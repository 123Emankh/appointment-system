package com.appointments.domain;

import java.time.LocalDateTime;

/**
 * Represents a notification message in the system.
 * <p>
 * A notification contains content, a timestamp indicating when it was created,
 * and a type describing the category of the message.
 * </p>
 *
 * @author Eman
 * @version 1.0
 */
public class NotificationMessage {

    private String content;
    private LocalDateTime timestamp;
    private String type;

    /**
     * Constructs a notification message with default type "GENERAL".
     *
     * @param content the content of the notification
     */
    public NotificationMessage(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.type = "GENERAL";
    }

    /**
     * Constructs a notification message with a specified type.
     *
     * @param content the content of the notification
     * @param type the type/category of the notification
     */
    public NotificationMessage(String content, String type) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.type = type;
    }

    /**
     * Returns the content of the notification.
     *
     * @return the notification content
     */
    public String getContent() { return content; }

    /**
     * Returns the timestamp when the notification was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Returns the type of the notification.
     *
     * @return the notification type
     */
    public String getType() { return type; }

    /**
     * Sets the content of the notification.
     *
     * @param content the new notification content
     */
    public void setContent(String content) { this.content = content; }

    /**
     * Sets the type of the notification.
     *
     * @param type the new notification type
     */
    public void setType(String type) { this.type = type; }

    /**
     * Returns a string representation of the notification.
     *
     * @return formatted notification string
     */
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp, type, content);
    }
}