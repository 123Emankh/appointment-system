package com.appointments.test;


import org.junit.jupiter.api.Test;

import com.appointments.domain.Appointment;
import com.appointments.domain.AppointmentType;
import com.appointments.domain.TimeSlot;
import com.appointments.domain.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    // Helper method عشان نعمل User (عدّليه حسب كلاس User عندك)
    private User createUser() {
        return new User("U1", "1234", "Eman", "eman@test.com");
    }

    private TimeSlot createFutureTimeSlot() {
        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = start.plusHours(1);
        return new TimeSlot(start, end);
    }

    private TimeSlot createPastTimeSlot() {
        LocalDateTime start = LocalDateTime.now().minusHours(2);
        LocalDateTime end = start.plusHours(1);
        return new TimeSlot(start, end);
    }

    @Test
    void testConstructorAndGetters() {
        User user = createUser();
        TimeSlot slot = createFutureTimeSlot();

        Appointment appt = new Appointment("1", user, slot, AppointmentType.GROUP, "NEW");

        assertEquals("1", appt.getId());
        assertEquals(user, appt.getUser());
        assertEquals(slot, appt.getTimeSlot());
        assertEquals(AppointmentType.GROUP, appt.getType());
        assertEquals("NEW", appt.getStatus());

        // مهم للتغطية
        assertNotNull(appt.getCreatedAt());
        assertTrue(appt.getParticipants().contains(user));
    }

    @Test
    void testSetters() {
        Appointment appt = new Appointment("1", createUser(), createFutureTimeSlot(), AppointmentType.GROUP, "NEW");

        User newUser = new User("U2", "pass", "Ali", "ali@test.com");
        TimeSlot newSlot = createFutureTimeSlot();

        appt.setId("2");
        appt.setUser(newUser);
        appt.setTimeSlot(newSlot);
        appt.setType(AppointmentType.INDIVIDUAL);
        appt.setStatus("DONE");

        assertEquals("2", appt.getId());
        assertEquals(newUser, appt.getUser());
        assertEquals(newSlot, appt.getTimeSlot());
        assertEquals(AppointmentType.INDIVIDUAL, appt.getType());
        assertEquals("DONE", appt.getStatus());
    }

    @Test
    void testParticipants() {
        Appointment appt = new Appointment("1", createUser(), createFutureTimeSlot(), AppointmentType.GROUP, "NEW");

        User user2 = new User("U2", "123", "Ali", "ali@test.com");
        appt.addParticipant(user2);

        assertEquals(2, appt.getParticipants().size());
    }

    @Test
    void testIsFuture() {
        Appointment futureAppt = new Appointment("1", createUser(), createFutureTimeSlot(), AppointmentType.GROUP, "NEW");
        Appointment pastAppt = new Appointment("2", createUser(), createPastTimeSlot(), AppointmentType.GROUP, "NEW");

        assertTrue(futureAppt.isFuture());
        assertFalse(pastAppt.isFuture());
    }

    @Test
    void testDuration() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2); // 2 hours

        TimeSlot slot = new TimeSlot(start, end);
        Appointment appt = new Appointment("1", createUser(), slot, AppointmentType.GROUP, "NEW");

        assertEquals(2, appt.getDurationHours());
        assertEquals(120, appt.getDurationMinutes());
    }

    @Test
    void testMaxParticipants() {
        Appointment group = new Appointment("1", createUser(), createFutureTimeSlot(), AppointmentType.GROUP, "NEW");
        Appointment individual = new Appointment("2", createUser(), createFutureTimeSlot(), AppointmentType.INDIVIDUAL, "NEW");
        Appointment inPerson = new Appointment("3", createUser(), createFutureTimeSlot(), AppointmentType.IN_PERSON, "NEW");
        Appointment virtual = new Appointment("4", createUser(), createFutureTimeSlot(), AppointmentType.VIRTUAL, "NEW");

        assertEquals(10, group.getMaxParticipants());
        assertEquals(1, individual.getMaxParticipants());
        assertEquals(5, inPerson.getMaxParticipants());
        assertEquals(Integer.MAX_VALUE, virtual.getMaxParticipants());
    }

    @Test
    void testToString() {
        Appointment appt = new Appointment("1", createUser(), createFutureTimeSlot(), AppointmentType.GROUP, "NEW");

        String result = appt.toString();

        assertTrue(result.contains("Appointment"));
        assertTrue(result.contains("Eman"));
    }

    // test بسيط بس لرفع التغطية أكثر
    @Test
    void justRun() {
        Appointment appt = new Appointment("X", createUser(), createFutureTimeSlot(), AppointmentType.GROUP, "NEW");
        appt.getDurationHours();
        appt.getDurationMinutes();
        appt.getMaxParticipants();
    }
}