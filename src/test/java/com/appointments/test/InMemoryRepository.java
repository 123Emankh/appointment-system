package com.appointments.test;

import com.appointments.domain.*;
import com.appointments.persistence.InMemoryRepository;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRepositoryTest {

    private User createUser() {
        return new User("testUser", "1234", "Test User", "test@test.com");
    }

    private TimeSlot createSlot() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        return new TimeSlot(start, end);
    }

    @Test
    void testConstructorInitialData() {
        InMemoryRepository repo = new InMemoryRepository();

        assertNotNull(repo.findAdministrator("admin"));
        assertNotNull(repo.findUser("eman"));

        assertTrue(repo.getAvailableSlots().size() > 0);
    }

    @Test
    void testUserOperations() {
        InMemoryRepository repo = new InMemoryRepository();

        User user = createUser();
        repo.saveUser(user);

        assertEquals(user, repo.findUser("testUser"));
        assertTrue(repo.getAllUsers().contains(user));
    }

    @Test
    void testAdministratorOperations() {
        InMemoryRepository repo = new InMemoryRepository();

        Administrator admin = new Administrator("a1", "pass", "Admin");
        repo.saveAdministrator(admin);

        assertEquals(admin, repo.findAdministrator("a1"));
    }

    @Test
    void testAppointmentOperations() {
        InMemoryRepository repo = new InMemoryRepository();

        User user = createUser();
        TimeSlot slot = createSlot();

        Appointment appt = new Appointment("1", user, slot, AppointmentType.GROUP, "NEW");

        repo.saveAppointment(appt);

        assertEquals(appt, repo.findAppointment("1"));
        assertTrue(repo.getAllAppointments().contains(appt));

        repo.removeAppointment("1");
        assertNull(repo.findAppointment("1"));
    }

    @Test
    void testGetUserAppointments() {
        InMemoryRepository repo = new InMemoryRepository();

        User user = createUser();
        TimeSlot slot = createSlot();

        Appointment appt1 = new Appointment("1", user, slot, AppointmentType.GROUP, "NEW");
        Appointment appt2 = new Appointment("2", user, slot, AppointmentType.GROUP, "NEW");

        repo.saveAppointment(appt1);
        repo.saveAppointment(appt2);

        assertEquals(2, repo.getUserAppointments(user).size());
    }

    @Test
    void testTimeSlotOperations() {
        InMemoryRepository repo = new InMemoryRepository();

        TimeSlot slot = createSlot();
        repo.addAvailableTimeSlot(slot);

        assertTrue(repo.getAvailableSlots().contains(slot));

        repo.removeTimeSlot(slot);
        assertFalse(repo.getAvailableSlots().contains(slot));
    }

    @Test
    void testIsSlotOverlapping() {
        InMemoryRepository repo = new InMemoryRepository();

        LocalDateTime now = LocalDateTime.now();

        TimeSlot slot1 = new TimeSlot(now.plusHours(1), now.plusHours(2));
        TimeSlot slot2 = new TimeSlot(now.plusHours(1).plusMinutes(30), now.plusHours(2).plusMinutes(30));

        repo.addAvailableTimeSlot(slot1);

        assertTrue(repo.isSlotOverlapping(slot2));
    }

    @Test
    void justRun() {
        InMemoryRepository repo = new InMemoryRepository();

        repo.getAllUsers();
        repo.getAllAppointments();
        repo.getAvailableSlots();
    }
}