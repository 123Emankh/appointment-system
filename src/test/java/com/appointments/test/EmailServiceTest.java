package com.appointments.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import com.appointments.service.EmailService;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmailService Tests")
class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService("test@gmail.com", "fake-password");
    }

    @Test
    @DisplayName("Should create EmailService instance")
    void testConstructor() {
        assertNotNull(emailService);
    }

    @Test
    @DisplayName("Should throw exception when sending with invalid credentials")
    void testSendEmailWithInvalidCredentials() {
        assertThrows(RuntimeException.class, () -> {
            emailService.sendEmail("test@example.com", "Test Subject", "Test Body");
        });
    }

    @Test
    @DisplayName("Should handle null recipient")
    void testSendEmailWithNullRecipient() {
        assertThrows(RuntimeException.class, () -> {
            emailService.sendEmail(null, "Subject", "Body");
        });
    }

    @Test
    @DisplayName("Should handle empty recipient")
    void testSendEmailWithEmptyRecipient() {
        assertThrows(RuntimeException.class, () -> {
            emailService.sendEmail("", "Subject", "Body");
        });
    }
}