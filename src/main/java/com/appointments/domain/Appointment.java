package com.appointments.domain;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private String id;
    private User user;
    private TimeSlot timeSlot;
    private AppointmentType type;
    private String status;
    private List<User> participants;
    private LocalDateTime createdAt;

    public Appointment(String id, User user, TimeSlot timeSlot, AppointmentType type, String status) {
        this.id = id;
        this.user = user;
        this.timeSlot = timeSlot;
        this.type = type;
        this.status = status;
        this.participants = new ArrayList<>();
        this.participants.add(user);
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public TimeSlot getTimeSlot() { return timeSlot; }
    public void setTimeSlot(TimeSlot timeSlot) { this.timeSlot = timeSlot; }

    public AppointmentType getType() { return type; }
    public void setType(AppointmentType type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<User> getParticipants() { return participants; }
    public void addParticipant(User participant) { 
        this.participants.add(participant); 
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public boolean isFuture() {
        return timeSlot.getStart().isAfter(LocalDateTime.now());
    }

    public long getDurationHours() {
        return Duration.between(timeSlot.getStart(), timeSlot.getEnd()).toHours();
    }
    
    public long getDurationMinutes() {
        return Duration.between(timeSlot.getStart(), timeSlot.getEnd()).toMinutes();
    }

    // دالة جديدة لإرجاع max participants حسب النوع
    public int getMaxParticipants() {
        switch (type) {
            case GROUP:
                return 10;
            case INDIVIDUAL:
                return 1;
            case IN_PERSON:
                return 5;
            case VIRTUAL:
                return Integer.MAX_VALUE; // أو قيمة كبيرة
            default:
                return 5; // قيمة افتراضية
        }
    }

    @Override
    public String toString() {
        return String.format("Appointment{id='%s', user=%s, type=%s, time=%s, status='%s'}",
            id, user != null ? user.getName() : "null", type, timeSlot, status);
    }
}