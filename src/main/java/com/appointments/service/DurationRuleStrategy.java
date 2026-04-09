package com.appointments.service;

import com.appointments.domain.Appointment;

/**
 * Strategy that enforces a maximum duration limit for appointments.
 * The duration is measured in minutes.
 *
 * @author Eman Kh
 * @version 1.0
 */
public class DurationRuleStrategy implements BookingRuleStrategy {
    private int maxDuration; // in minutes

    /**
     * Constructs a duration rule with a specified maximum duration.
     *
     * @param maxDuration the maximum allowed duration in minutes
     */
    public DurationRuleStrategy(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    /**
     * Validates that the appointment's duration does not exceed the maximum allowed.
     *
     * @param appointment the appointment to validate
     * @return true if appointment duration is less than or equal to maxDuration, false otherwise
     */
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDurationMinutes() <= maxDuration;
    }
}