package com.appointments.domain;

/**
 * Enumeration representing the different types of appointments supported by the system.
 * Each type may have specific rules for duration, participant limits, and scheduling constraints.
 *
 * @author Eman 
 * @version 1.0
 */
public enum AppointmentType {
    /** Urgent appointment that must be scheduled within 24 hours. */
    URGENT,
    /** Follow-up appointment, typically requires a previous appointment. */
    FOLLOW_UP,
    /** Assessment appointment, usually requires business hours (9-17). */
    ASSESSMENT,
    /** Virtual appointment conducted online. */
    VIRTUAL,
    /** In-person appointment at a physical location. */
    IN_PERSON,
    /** Individual appointment for a single participant. */
    INDIVIDUAL,
    /** Group appointment allowing multiple participants. */
    GROUP
}