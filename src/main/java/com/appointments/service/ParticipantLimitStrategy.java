package com.appointments.service;

import com.appointments.domain.Appointment;

public class ParticipantLimitStrategy implements BookingRuleStrategy {
    @Override
    public boolean isValid(Appointment appointment) {
        int participantCount = appointment.getParticipants().size();
        switch (appointment.getType()) {
            case GROUP:
                return participantCount <= 10;
            case INDIVIDUAL:
                return participantCount <= 1;
            case IN_PERSON:
                return participantCount <= 5;
            case VIRTUAL:
                return true; // no limit
            default:
                return participantCount <= 5;
        }
    }
}