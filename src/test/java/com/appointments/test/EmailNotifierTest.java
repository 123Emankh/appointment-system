package com.appointments.test;

import com.appointments.domain.User;
import com.appointments.service.EmailNotifier;
import com.appointments.service.EmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

@DisplayName("EmailNotifier Tests")
class EmailNotifierTest {

    private EmailService emailService;
    private EmailNotifier emailNotifier;
    private User testUser;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        emailNotifier = new EmailNotifier(emailService);
        testUser = new User("testuser", "password", "Test User", "test@example.com");
    }

    @Test
    @DisplayName("Should send email when user has valid email")
    void testNotifyWithValidUser() {
        emailNotifier.notify(testUser, "Test message");
        
        verify(emailService, times(1)).sendEmail(
            eq("test@example.com"),
            eq("Appointment Reminder"),
            eq("Test message")
        );
    }

    @Test
    @DisplayName("Should not send email when user is null")
    void testNotifyWithNullUser() {
        emailNotifier.notify(null, "Test message");
        
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should not send email when user email is null")
    void testNotifyWithNullEmail() {
        User userWithoutEmail = new User("test", "pass", "Test", null);
        emailNotifier.notify(userWithoutEmail, "Test message");
        
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should not send email when user email is empty")
    void testNotifyWithEmptyEmail() {
        User userWithEmptyEmail = new User("test", "pass", "Test", "");
        emailNotifier.notify(userWithEmptyEmail, "Test message");
        
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should handle null message gracefully")
    void testNotifyWithNullMessage() {
        emailNotifier.notify(testUser, null);
        
        verify(emailService, times(1)).sendEmail(
            eq("test@example.com"),
            eq("Appointment Reminder"),
            eq(null)
        );
    }

    @Test
    @DisplayName("Should handle empty message gracefully")
    void testNotifyWithEmptyMessage() {
        emailNotifier.notify(testUser, "");
        
        verify(emailService, times(1)).sendEmail(
            eq("test@example.com"),
            eq("Appointment Reminder"),
            eq("")
        );
    }
}