package com.appointments.service;

import com.appointments.domain.Appointment;
import java.time.LocalDateTime;

/**
 * Booking rule that enforces constraints based on appointment type.
 * <ul>
 *   <li>URGENT: must be scheduled within 24 hours</li>
 *   <li>ASSESSMENT: must be during business hours (9:00 - 17:00)</li>
 *   <li>FOLLOW_UP: no specific constraint (simplified)</li>
 * </ul>
 *
 * @author Eman
 * @version 1.0
 */
public class TypeSpecificRuleStrategy implements BookingRuleStrategy {
    /**
     * Validates type-specific constraints for the appointment.
     *
     * @param appointment the appointment to validate
     * @return true if all type-specific rules are satisfied, false otherwise
     */
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
    
    /**
     * Default constructor.
     */
    public TypeSpecificRuleStrategy() {
    }
}