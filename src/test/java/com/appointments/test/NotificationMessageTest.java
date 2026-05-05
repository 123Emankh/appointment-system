package com.appointments.test;

import org.junit.jupiter.api.Test;

import com.appointments.domain.NotificationMessage;


import static org.junit.jupiter.api.Assertions.*;

class NotificationMessageTest {

    @Test
    void testConstructorWithDefaultType() {
        NotificationMessage msg = new NotificationMessage("Hello");

        assertEquals("Hello", msg.getContent());
        assertEquals("GENERAL", msg.getType());
        assertNotNull(msg.getTimestamp());
    }

    @Test
    void testConstructorWithCustomType() {
        NotificationMessage msg = new NotificationMessage("Hi", "INFO");

        assertEquals("Hi", msg.getContent());
        assertEquals("INFO", msg.getType());
        assertNotNull(msg.getTimestamp());
    }

    @Test
    void testSetters() {
        NotificationMessage msg = new NotificationMessage("Old");

        msg.setContent("New Content");
        msg.setType("WARNING");

        assertEquals("New Content", msg.getContent());
        assertEquals("WARNING", msg.getType());
    }

    @Test
    void testToString() {
        NotificationMessage msg = new NotificationMessage("Test", "INFO");

        String result = msg.toString();

        assertTrue(result.contains("INFO"));
        assertTrue(result.contains("Test"));
    }

    @Test
    void justRun() {
        NotificationMessage msg = new NotificationMessage("X");
        msg.getContent();
        msg.getType();
        msg.getTimestamp();
    }
}