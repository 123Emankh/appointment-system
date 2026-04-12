package com.appointments.service;

import com.appointments.domain.Appointment;

/**
 * Booking rule that validates participant limits based on appointment type.
 * <ul>
 *   <li>GROUP: max 10 participants</li>
 *   <li>INDIVIDUAL: max 1 participant</li>
 *   <li>IN_PERSON: max 5 participants</li>
 *   <li>VIRTUAL: no limit</li>
 * </ul>
 *
 * @author Eman
 * @version 1.0
 */
public class ParticipantLimitStrategy implements BookingRuleStrategy {
    /**
     * Validates that the number of participants does not exceed the limit for the appointment type.
     *
     * @param appointment the appointment to validate
     * @return true if participant count is within allowed limits, false otherwise
     */
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
                return true; 
            
            default:
                return participantCount <= 5;
        }
    }
    /**
     * Default constructor.
     */
    public ParticipantLimitStrategy() {
    }
}