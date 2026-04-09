package com.appointments.test;


import org.junit.jupiter.api.Test;

import com.appointments.domain.Appointment;
import com.appointments.domain.AppointmentType;
import com.appointments.domain.Schedule;
import com.appointments.domain.TimeSlot;
import com.appointments.domain.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    private User createUser() {
        return new User("U1", "1234", "Eman", "eman@test.com");
    }

    private TimeSlot createSlot() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        return new TimeSlot(start, end);
    }

    @Test
    void testConstructor() {
        Schedule schedule = new Schedule();

        assertNotNull(schedule.getAvailableSlots());
        assertNotNull(schedule.getAppointments());
        assertEquals(0, schedule.getAvailableSlots().size());
        assertEquals(0, schedule.getAppointments().size());
    }

    @Test
    void testAddAvailableSlot() {
        Schedule schedule = new Schedule();
        TimeSlot slot = createSlot();

        schedule.addAvailableSlot(slot);

        assertEquals(1, schedule.getAvailableSlots().size());
        assertTrue(schedule.getAvailableSlots().contains(slot));
    }

    @Test
    void testAddAppointment() {
        Schedule schedule = new Schedule();

        Appointment appt = new Appointment(
                "1",
                createUser(),
                createSlot(),
                AppointmentType.GROUP,
                "NEW"
        );

        schedule.addAppointment(appt);

        assertEquals(1, schedule.getAppointments().size());
        assertTrue(schedule.getAppointments().contains(appt));
    }

    @Test
    void testGetters() {
        Schedule schedule = new Schedule();

        assertNotNull(schedule.getAvailableSlots());
        assertNotNull(schedule.getAppointments());
    }

    @Test
    void justRun() {
        Schedule schedule = new Schedule();
        schedule.getAvailableSlots();
        schedule.getAppointments();
    }
}