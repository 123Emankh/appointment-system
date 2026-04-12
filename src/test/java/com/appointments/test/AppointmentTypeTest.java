package com.appointments.test;


import org.junit.jupiter.api.Test;

import com.appointments.domain.AppointmentType;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTypeTest {

    @Test
    void testEnumValues() {
        AppointmentType[] values = AppointmentType.values();

        assertNotNull(values);
        assertTrue(values.length > 0);
    }

    @Test
    void testValueOf() {
        AppointmentType type = AppointmentType.valueOf("GROUP");

        assertEquals(AppointmentType.GROUP, type);
    }

    @Test
    void testAllEnumConstants() {
        assertEquals("URGENT", AppointmentType.URGENT.name());
        assertEquals("FOLLOW_UP", AppointmentType.FOLLOW_UP.name());
        assertEquals("ASSESSMENT", AppointmentType.ASSESSMENT.name());
        assertEquals("VIRTUAL", AppointmentType.VIRTUAL.name());
        assertEquals("IN_PERSON", AppointmentType.IN_PERSON.name());
        assertEquals("INDIVIDUAL", AppointmentType.INDIVIDUAL.name());
        assertEquals("GROUP", AppointmentType.GROUP.name());
    }

    @Test
    void justRun() {
        for (AppointmentType type : AppointmentType.values()) {
            type.name();
        }
    }
}