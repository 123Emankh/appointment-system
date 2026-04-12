package com.appointments.test;

import org.junit.jupiter.api.Test;

import com.appointments.domain.TimeSlot;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    private TimeSlot createSlot1() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);
        return new TimeSlot(start, end);
    }

    private TimeSlot createSlot2() {
        LocalDateTime start = LocalDateTime.now().plusHours(4);  
        LocalDateTime end = start.plusHours(2);
        return new TimeSlot(start, end);
    }

    @Test
    void testConstructorValid() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);

        TimeSlot slot = new TimeSlot(start, end);

        assertEquals(start, slot.getStart());
        assertEquals(end, slot.getEnd());
        assertTrue(slot.isAvailable());
    }

    @Test
    void testConstructorInvalidThrowsException() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusHours(1);

        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot(start, end);
        });
    }

    @Test
    void testAvailabilitySetter() {
        TimeSlot slot = createSlot1();

        slot.setAvailable(false);

        assertFalse(slot.isAvailable());
    }

    @Test
    void testDuration() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(3);

        TimeSlot slot = new TimeSlot(start, end);

        assertEquals(3, slot.getDurationHours());
        assertEquals(180, slot.getDurationMinutes());
    }

    @Test
    void testOverlapsTrue() {
        LocalDateTime start1 = LocalDateTime.now().plusHours(1);
        LocalDateTime end1 = start1.plusHours(3);

        LocalDateTime start2 = start1.plusHours(2);
        LocalDateTime end2 = start2.plusHours(2);

        TimeSlot slot1 = new TimeSlot(start1, end1);
        TimeSlot slot2 = new TimeSlot(start2, end2);

        assertTrue(slot1.overlaps(slot2));
    }

    @Test
    void testOverlapsFalse() {
        TimeSlot slot1 = createSlot1();  
        TimeSlot slot2 = createSlot2();  

        assertFalse(slot1.overlaps(slot2));
    }

    @Test
    void testToString() {
        TimeSlot slot = createSlot1();

        String result = slot.toString();

        assertNotNull(result);
        assertTrue(result.contains("-"));
    }

    @Test
    void justRun() {
        TimeSlot slot = createSlot1();
        slot.getStart();
        slot.getEnd();
        slot.getDurationHours();
        slot.getDurationMinutes();
        slot.isAvailable();
        slot.toString();
    }
}