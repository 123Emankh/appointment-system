package com.appointments.test;


import org.junit.jupiter.api.Test;

import com.appointments.domain.User;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructorAndGetters() {
        User user = new User("u1", "pass", "Eman", "eman@test.com");

        assertEquals("u1", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("Eman", user.getName());
        assertEquals("eman@test.com", user.getEmail());
        assertNull(user.getPhoneNumber()); // not set in constructor
    }

    @Test
    void testSetters() {
        User user = new User("u1", "pass", "Eman", "eman@test.com");

        user.setUsername("u2");
        user.setPassword("newpass");
        user.setName("Ali");
        user.setEmail("ali@test.com");
        user.setPhoneNumber("0591234567");

        assertEquals("u2", user.getUsername());
        assertEquals("newpass", user.getPassword());
        assertEquals("Ali", user.getName());
        assertEquals("ali@test.com", user.getEmail());
        assertEquals("0591234567", user.getPhoneNumber());
    }

    @Test
    void testEqualsSameObject() {
        User user = new User("u1", "pass", "Eman", "eman@test.com");

        assertEquals(user, user);
    }

    @Test
    void testEqualsEqualUsers() {
        User user1 = new User("u1", "pass", "Eman", "eman@test.com");
        User user2 = new User("u1", "123", "Other", "other@test.com");

        assertEquals(user1, user2);
    }

    @Test
    void testEqualsDifferentUsers() {
        User user1 = new User("u1", "pass", "Eman", "eman@test.com");
        User user2 = new User("u2", "pass", "Eman", "eman@test.com");

        assertNotEquals(user1, user2);
    }

    @Test
    void testEqualsNull() {
        User user = new User("u1", "pass", "Eman", "eman@test.com");

        assertNotEquals(null, user);
    }

    @Test
    void testHashCodeConsistency() {
        User user1 = new User("u1", "pass", "Eman", "eman@test.com");
        User user2 = new User("u1", "123", "Other", "other@test.com");

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void justRun() {
        User user = new User("x", "y", "z", "z@test.com");

        user.getUsername();
        user.getPassword();
        user.getName();
        user.getEmail();
        user.getPhoneNumber();
        user.hashCode();
    }
}