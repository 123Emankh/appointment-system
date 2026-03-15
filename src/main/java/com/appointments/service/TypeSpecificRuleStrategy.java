package com.appointments.service;

import com.appointments.domain.Appointment;
import java.time.LocalDateTime;

public class TypeSpecificRuleStrategy implements BookingRuleStrategy {
    @Override
    public boolean isValid(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        switch (appointment.getType()) {
            case URGENT:
                // urgent must be within 24 hours
                return appointment.getTimeSlot().getStart()
                    .isBefore(now.plusHours(24));
            case FOLLOW_UP:
                // follow-up requires previous appointment (simplified)
                return true;
            case ASSESSMENT:
                // assessment during business hours 9-17
                int hour = appointment.getTimeSlot().getStart().getHour();
                return hour >= 9 && hour <= 17;
            default:
                return true;
        }
    }
}