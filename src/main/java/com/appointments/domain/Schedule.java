package com.appointments.domain;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Appointment> appointments;

    public Schedule() {
        this.appointments = new ArrayList<>();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    // دوال أخرى حسب الحاجة
}