package com.appointments.test;

import com.appointments.domain.Appointment;
import com.appointments.domain.AppointmentType;
import com.appointments.domain.TimeSlot;
import com.appointments.domain.User;
import com.appointments.service.DurationRuleStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DurationRuleStrategy Tests")
class DurationRuleStrategyTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password", "Test User", "test@example.com");
    }

    private Appointment createAppointmentWithDuration(int durationMinutes) {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusMinutes(durationMinutes);
        TimeSlot slot = new TimeSlot(start, end);
        String id = UUID.randomUUID().toString();
        return new Appointment(id, testUser, slot, AppointmentType.INDIVIDUAL, "SCHEDULED");
    }

    @Test
    @DisplayName("Should allow appointment within duration limit (60 minutes)")
    void testValidDurationWithinLimit() {
        DurationRuleStrategy strategy = new DurationRuleStrategy(120);
        Appointment appointment = createAppointmentWithDuration(60);
        
        boolean result = strategy.isValid(appointment);
        
        assertTrue(result, "Appointment with 60 minutes should be valid for 120 minutes limit");
    }

    @Test
    @DisplayName("Should allow appointment exactly at duration limit")
    void testValidDurationExactlyAtLimit() {
        DurationRuleStrategy strategy = new DurationRuleStrategy(120);
        Appointment appointment = createAppointmentWithDuration(120);
        
        boolean result = strategy.isValid(appointment);
        
        assertTrue(result, "Appointment with exactly 120 minutes should be valid");
    }

    @Test
    @DisplayName("Should reject appointment exceeding duration limit")
    void testInvalidDurationExceedsLimit() {
        DurationRuleStrategy strategy = new DurationRuleStrategy(120);
        Appointment appointment = createAppointmentWithDuration(121);
        
        boolean result = strategy.isValid(appointment);
        
        assertFalse(result, "Appointment with 121 minutes should be invalid for 120 minutes limit");
    }

    @Test
    @DisplayName("Should reject very long appointment")
    void testInvalidVeryLongDuration() {
        DurationRuleStrategy strategy = new DurationRuleStrategy(60);
        Appointment appointment = createAppointmentWithDuration(180);
        
        boolean result = strategy.isValid(appointment);
        
        assertFalse(result, "Appointment with 180 minutes should be invalid for 60 minutes limit");
    }

    @Test
    @DisplayName("Should work with different duration limits")
    void testDifferentLimits() {
        // Limit 30 minutes
        DurationRuleStrategy strategy30 = new DurationRuleStrategy(30);
        Appointment app30min = createAppointmentWithDuration(30);
        Appointment app31min = createAppointmentWithDuration(31);
        
        assertTrue(strategy30.isValid(app30min), "30 min appointment should be valid for 30 min limit");
        assertFalse(strategy30.isValid(app31min), "31 min appointment should be invalid for 30 min limit");
        
        // Limit 240 minutes (4 hours)
        DurationRuleStrategy strategy240 = new DurationRuleStrategy(240);
        Appointment app240min = createAppointmentWithDuration(240);
        Appointment app241min = createAppointmentWithDuration(241);
        
        assertTrue(strategy240.isValid(app240min), "240 min appointment should be valid for 240 min limit");
        assertFalse(strategy240.isValid(app241min), "241 min appointment should be invalid for 240 min limit");
    }

    @Test
    @DisplayName("Should handle very small duration limit (1 minute)")
    void testSmallDurationLimit() {
        DurationRuleStrategy strategy = new DurationRuleStrategy(1);
        Appointment appointment = createAppointmentWithDuration(1);
        
        boolean result = strategy.isValid(appointment);
        assertTrue(result, "1 minute appointment should be valid for 1 minute limit");
        
        Appointment appointment2 = createAppointmentWithDuration(2);
        boolean result2 = strategy.isValid(appointment2);
        assertFalse(result2, "2 minute appointment should be invalid for 1 minute limit");
    }

    @Test
    @DisplayName("Should handle different appointment types")
    void testDifferentAppointmentTypes() {
        DurationRuleStrategy strategy = new DurationRuleStrategy(120);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusMinutes(60);
        TimeSlot slot = new TimeSlot(start, end);
        String id = UUID.randomUUID().toString();
        
        // Test with GROUP type
        Appointment groupApp = new Appointment(id, testUser, slot, AppointmentType.GROUP, "SCHEDULED");
        assertTrue(strategy.isValid(groupApp), "GROUP appointment within limit should be valid");
        
        // Test with IN_PERSON type
        Appointment inPersonApp = new Appointment(id + "1", testUser, slot, AppointmentType.IN_PERSON, "SCHEDULED");
        assertTrue(strategy.isValid(inPersonApp), "IN_PERSON appointment within limit should be valid");
        
        // Test with VIRTUAL type
        Appointment virtualApp = new Appointment(id + "2", testUser, slot, AppointmentType.VIRTUAL, "SCHEDULED");
        assertTrue(strategy.isValid(virtualApp), "VIRTUAL appointment within limit should be valid");
    }

    @Test
    @DisplayName("Should throw exception when appointment is null")
    void testNullAppointment() {
        DurationRuleStrategy strategy = new DurationRuleStrategy(120);
        
        assertThrows(NullPointerException.class, () -> {
            strategy.isValid(null);
        });
    }

    @Test
    @DisplayName("Should handle appointment with 1 minute duration")
    void testOneMinuteDuration() {
        DurationRuleStrategy strategy = new DurationRuleStrategy(1);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusMinutes(1);
        TimeSlot slot = new TimeSlot(start, end);
        String id = UUID.randomUUID().toString();
        Appointment appointment = new Appointment(id, testUser, slot, AppointmentType.INDIVIDUAL, "SCHEDULED");
        
        boolean result = strategy.isValid(appointment);
        assertTrue(result, "1 minute appointment should be valid");
    }
}