package com.appointments.domain;

import java.time.LocalDateTime;

public class NotificationMessage {
    private String content;
    private LocalDateTime timestamp;
    private String type;

    public NotificationMessage(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.type = "GENERAL";
    }

    public NotificationMessage(String content, String type) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.type = type;
    }

    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getType() { return type; }
    public void setContent(String content) { this.content = content; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp, type, content);
    }
}