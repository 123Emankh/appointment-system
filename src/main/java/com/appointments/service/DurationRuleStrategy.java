package com.appointments.service;

import com.appointments.domain.Appointment;

public class DurationRuleStrategy implements BookingRuleStrategy {
    private int maxDuration; // بالدقائق

    public DurationRuleStrategy(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDurationMinutes() <= maxDuration;
    }
}