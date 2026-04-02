package com.appointments.domain;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<TimeSlot> availableSlots;
    private List<Appointment> appointments;

    public Schedule() {
        this.availableSlots = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    public void addAvailableSlot(TimeSlot slot) {
        availableSlots.add(slot);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public List<TimeSlot> getAvailableSlots() {
        return availableSlots;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}