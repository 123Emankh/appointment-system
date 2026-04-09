package com.appointments.test;

import com.appointments.domain.Appointment;
import com.appointments.domain.AppointmentType;
import com.appointments.domain.TimeSlot;
import com.appointments.domain.User;
import com.appointments.service.ParticipantLimitStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParticipantLimitStrategy Tests")
class ParticipantLimitStrategyTest {

    private ParticipantLimitStrategy strategy;
    private User owner;
    private TimeSlot timeSlot;

    @BeforeEach
    void setUp() {
        strategy = new ParticipantLimitStrategy();
        owner = new User("owner", "password", "Owner User", "owner@example.com");
        timeSlot = new TimeSlot(
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(1)
        );
    }

    private Appointment createAppointmentWithParticipants(AppointmentType type, int participantCount) {
        String id = UUID.randomUUID().toString();
        Appointment appointment = new Appointment(id, owner, timeSlot, type, "SCHEDULED");
        
        for (int i = 1; i < participantCount; i++) {
            User participant = new User(
                "user" + i,
                "password" + i,
                "User " + i,
                "user" + i + "@example.com"
            );
            appointment.addParticipant(participant);
        }
        
        return appointment;
    }

    @Test
    @DisplayName("GROUP appointment: should allow up to 10 participants")
    void testGroupLimitValid() {
        for (int i = 1; i <= 10; i++) {
            Appointment appointment = createAppointmentWithParticipants(AppointmentType.GROUP, i);
            assertTrue(strategy.isValid(appointment), 
                "GROUP appointment with " + i + " participants should be valid");
        }
    }

    @Test
    @DisplayName("GROUP appointment: should reject more than 10 participants")
    void testGroupLimitExceeded() {
        Appointment appointment = createAppointmentWithParticipants(AppointmentType.GROUP, 11);
        assertFalse(strategy.isValid(appointment), "GROUP with 11 participants should be invalid");
    }

    @Test
    @DisplayName("INDIVIDUAL appointment: should allow only 1 participant")
    void testIndividualLimitValid() {
        Appointment appointment = createAppointmentWithParticipants(AppointmentType.INDIVIDUAL, 1);
        assertTrue(strategy.isValid(appointment), "INDIVIDUAL with 1 participant should be valid");
    }

    @Test
    @DisplayName("INDIVIDUAL appointment: should reject more than 1 participant")
    void testIndividualLimitExceeded() {
        Appointment appointment = createAppointmentWithParticipants(AppointmentType.INDIVIDUAL, 2);
        assertFalse(strategy.isValid(appointment), "INDIVIDUAL with 2 participants should be invalid");
    }

    @Test
    @DisplayName("IN_PERSON appointment: should allow up to 5 participants")
    void testInPersonLimitValid() {
        for (int i = 1; i <= 5; i++) {
            Appointment appointment = createAppointmentWithParticipants(AppointmentType.IN_PERSON, i);
            assertTrue(strategy.isValid(appointment), 
                "IN_PERSON appointment with " + i + " participants should be valid");
        }
    }

    @Test
    @DisplayName("IN_PERSON appointment: should reject more than 5 participants")
    void testInPersonLimitExceeded() {
        Appointment appointment = createAppointmentWithParticipants(AppointmentType.IN_PERSON, 6);
        assertFalse(strategy.isValid(appointment), "IN_PERSON with 6 participants should be invalid");
    }

    @Test
    @DisplayName("VIRTUAL appointment: should allow any number of participants")
    void testVirtualLimit() {
        int[] counts = {1, 5, 10, 20, 50, 100};
        for (int count : counts) {
            Appointment appointment = createAppointmentWithParticipants(AppointmentType.VIRTUAL, count);
            assertTrue(strategy.isValid(appointment), 
                "VIRTUAL appointment with " + count + " participants should be valid");
        }
    }
}