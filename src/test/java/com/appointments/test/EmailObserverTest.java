package com.appointments.test;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.appointments.domain.User;
import com.appointments.service.EmailObserver;
import com.appointments.service.EmailService;

class EmailObserverTest {

    private EmailService mockEmailService;
    private EmailObserver observer;

    @BeforeEach 
    void setUp() {
        mockEmailService = mock(EmailService.class);
        observer = new EmailObserver(mockEmailService);
    }

    @Test
    void shouldSendEmailWhenUserHasEmail() {
        User user = TestDataFactory.createUserWithEmail("test@gmail.com");

        observer.notify(user, "Hello");

        verify(mockEmailService, times(1))
                .sendEmail(eq("test@gmail.com"), anyString(), eq("Hello"));
    }

    @Test
    void shouldNotSendEmailWhenEmailIsNull() {
        User user = TestDataFactory.createUserWithoutEmail();

        observer.notify(user, "Hello");

        verify(mockEmailService, never()).sendEmail(any(), any(), any());
    }
}