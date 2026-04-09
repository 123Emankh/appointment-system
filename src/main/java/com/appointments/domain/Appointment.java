package com.appointments.domain;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an appointment in the system.
 * <p>
 * An appointment contains information about the user, time slot,
 * type, status, participants, and creation time.
 * </p>
 *
 * @author Eman
 * @version 1.0
 */
public class Appointment {

    private String id;
    private User user;
    private TimeSlot timeSlot;
    private AppointmentType type;
    private String status;
    private List<User> participants;
    private LocalDateTime createdAt;

    /**
     * Constructs a new Appointment.
     *
     * @param id the unique identifier of the appointment
     * @param user the owner user of the appointment
     * @param timeSlot the scheduled time slot
     * @param type the type of the appointment
     * @param status the current status of the appointment
     */
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

    /**
     * Returns the appointment ID.
     *
     * @return the appointment ID
     */
    public String getId() { return id; }

    /**
     * Sets the appointment ID.
     *
     * @param id the new appointment ID
     */
    public void setId(String id) { this.id = id; }

    /**
     * Returns the owner user of the appointment.
     *
     * @return the user
     */
    public User getUser() { return user; }

    /**
     * Sets the owner user of the appointment.
     *
     * @param user the new user
     */
    public void setUser(User user) { this.user = user; }

    /**
     * Returns the time slot of the appointment.
     *
     * @return the timeSlot
     */
    public TimeSlot getTimeSlot() { return timeSlot; }

    /**
     * Sets the time slot of the appointment.
     *
     * @param timeSlot the new time slot
     */
    public void setTimeSlot(TimeSlot timeSlot) { this.timeSlot = timeSlot; }

    /**
     * Returns the type of the appointment.
     *
     * @return the appointment type
     */
    public AppointmentType getType() { return type; }

    /**
     * Sets the type of the appointment.
     *
     * @param type the new appointment type
     */
    public void setType(AppointmentType type) { this.type = type; }

    /**
     * Returns the status of the appointment.
     *
     * @return the status
     */
    public String getStatus() { return status; }

    /**
     * Sets the status of the appointment.
     *
     * @param status the new status
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Returns the list of participants in the appointment.
     *
     * @return list of participants
     */
    public List<User> getParticipants() { return participants; }

    /**
     * Adds a participant to the appointment.
     *
     * @param participant the user to add
     */
    public void addParticipant(User participant) { this.participants.add(participant); }

    /**
     * Returns the creation time of the appointment.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Checks if the appointment is scheduled in the future.
     *
     * @return true if the appointment is in the future, false otherwise
     */
    public boolean isFuture() {
        return timeSlot.getStart().isAfter(LocalDateTime.now());
    }

    /**
     * Returns the duration of the appointment in hours.
     *
     * @return duration in hours
     */
    public long getDurationHours() {
        return Duration.between(timeSlot.getStart(), timeSlot.getEnd()).toHours();
    }

    /**
     * Returns the duration of the appointment in minutes.
     *
     * @return duration in minutes
     */
    public long getDurationMinutes() {
        return Duration.between(timeSlot.getStart(), timeSlot.getEnd()).toMinutes();
    }

    /**
     * Returns the maximum number of participants allowed based on appointment type.
     *
     * @return maximum number of participants
     */
    public int getMaxParticipants() {
        switch (type) {
            case GROUP: return 10;
            case INDIVIDUAL: return 1;
            case IN_PERSON: return 5;
            case VIRTUAL: return Integer.MAX_VALUE;
            default: return 1;
        }
    }

    /**
     * Returns a string representation of the appointment.
     *
     * @return formatted appointment details
     */
    @Override
    public String toString() {
        return String.format("Appointment{id='%s', user=%s, type=%s, time=%s, status='%s'}",
            id, user.getName(), type, timeSlot, status);
    }
}