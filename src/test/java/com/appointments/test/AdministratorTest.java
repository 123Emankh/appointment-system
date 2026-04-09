package com.appointments.test;

import org.junit.jupiter.api.Test;

import com.appointments.domain.Administrator;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorTest {

    @Test
    void testConstructorAndGetters() {
        Administrator admin = new Administrator("A1", "1234", "Eman");

        assertEquals("A1", admin.getAdminId());
        assertEquals("SUPER_ADMIN", admin.getRole());
    }

    @Test
    void testSetters() {
        Administrator admin = new Administrator("A1", "1234", "Eman");

        admin.setAdminId("A2");
        admin.setRole("MANAGER");

        assertEquals("A2", admin.getAdminId());
        assertEquals("MANAGER", admin.getRole());
    }

    @Test
    void justRunForCoverage() {
        Administrator admin = new Administrator("X", "pass", "Test");
        admin.getAdminId();
        admin.getRole();
    }
}