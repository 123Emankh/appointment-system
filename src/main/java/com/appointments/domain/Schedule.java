package com.appointments.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the schedule of the system.
 * <p>
 * The Schedule class manages available time slots and booked appointments.
 * It acts as a container for scheduling data.
 * </p>
 *
 * @author Eman
 * @version 1.0
 */
public class Schedule {

    private List<TimeSlot> availableSlots;
    private List<Appointment> appointments;

    /**
     * Constructs a new Schedule with empty lists of slots and appointments.
     */
    public Schedule() {
        this.availableSlots = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    /**
     * Adds a new available time slot to the schedule.
     *
     * @param slot the time slot to be added
     */
    public void addAvailableSlot(TimeSlot slot) {
        availableSlots.add(slot);
    }

    /**
     * Adds a new appointment to the schedule.
     *
     * @param appointment the appointment to be added
     */
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Returns the list of available time slots.
     *
     * @return list of available slots
     */
    public List<TimeSlot> getAvailableSlots() {
        return availableSlots;
    }

    /**
     * Returns the list of all appointments.
     *
     * @return list of appointments
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }
}