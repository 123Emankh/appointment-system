package com.appointments.service;

import com.appointments.domain.Appointment;

/**
 * قاعدة المشاركين.
 * @author فريقك
 * @version 1.0
 */
public class ParticipantLimitStrategy implements BookingRuleStrategy {
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getParticipants().size() <= appointment.getMaxParticipants();
    }
}