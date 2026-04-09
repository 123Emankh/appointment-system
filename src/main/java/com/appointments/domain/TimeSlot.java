package com.appointments.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a time slot with a start and end time.
 * <p>
 * A TimeSlot defines a period of time that can be used for scheduling appointments.
 * It also tracks whether the slot is available or not.
 * </p>
 *
 * @author Eman
 * @version 1.0
 */
public class TimeSlot {

    private final LocalDateTime start;
    private final LocalDateTime end;
    private boolean available = true;

    /**
     * Constructs a TimeSlot with the specified start and end times.
     *
     * @param start the start time of the slot
     * @param end the end time of the slot
     * @throws IllegalArgumentException if start is after or equal to end
     */
    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.start = start;
        this.end = end;
        this.available = true;
    }

    /**
     * Returns the start time of the slot.
     *
     * @return the start time
     */
    public LocalDateTime getStart() { return start; }

    /**
     * Returns the end time of the slot.
     *
     * @return the end time
     */
    public LocalDateTime getEnd() { return end; }

    /**
     * Checks whether the time slot is available.
     *
     * @return true if available, false otherwise
     */
    public boolean isAvailable() { return available; }

    /**
     * Sets the availability status of the time slot.
     *
     * @param available the new availability status
     */
    public void setAvailable(boolean available) { this.available = available; }

    /**
     * Calculates the duration of the time slot in hours.
     *
     * @return duration in hours
     */
    public long getDurationHours() {
        return java.time.Duration.between(start, end).toHours();
    }

    /**
     * Calculates the duration of the time slot in minutes.
     *
     * @return duration in minutes
     */
    public long getDurationMinutes() {
        return java.time.Duration.between(start, end).toMinutes();
    }

    /**
     * Checks whether this time slot overlaps with another time slot.
     *
     * @param other the other time slot to compare with
     * @return true if the slots overlap, false otherwise
     */
    public boolean overlaps(TimeSlot other) {
        if (other == null) return false;

        return this.start.isBefore(other.end) && other.start.isBefore(this.end);
    }

    /**
     * Returns a formatted string representation of the time slot.
     *
     * @return formatted time range as a string
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return start.format(formatter) + " - " + end.format(formatter);
    }
}