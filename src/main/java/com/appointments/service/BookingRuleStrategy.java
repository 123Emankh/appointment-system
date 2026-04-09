package com.appointments.service;

import com.appointments.domain.Appointment;

/**
 * Strategy interface for booking rule validation.
 * Implementations define specific constraints that an appointment must satisfy
 * before being confirmed (e.g., duration limits, participant caps, type-specific rules).
 *
 * @author Eman 
 * @version 1.0
 */
public interface BookingRuleStrategy {
    /**
     * Validates the given appointment against a specific business rule.
     *
     * @param appointment the appointment to validate
     * @return true if the appointment satisfies the rule, false otherwise
     */
    boolean isValid(Appointment appointment);
}