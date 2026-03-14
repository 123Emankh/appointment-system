package com.appointments.service;

import com.appointments.domain.Appointment;

/**
 * قاعدة المدة.
 * @author فريقك
 * @version 1.0
 */
public class DurationRuleStrategy implements BookingRuleStrategy {
    private int maxDuration;

    public DurationRuleStrategy(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDurationMinutes() <= maxDuration;
    }
}