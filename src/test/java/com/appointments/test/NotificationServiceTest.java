package com.appointments.test;

import com.appointments.domain.NotificationMessage;
import com.appointments.domain.User;
import com.appointments.service.EmailService;
import com.appointments.service.NotificationService;
import com.appointments.service.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("NotificationService Tests")
class NotificationServiceTest {

    private EmailService emailService;
    private NotificationService notificationService;
    private User testUser;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        notificationService = new NotificationService(emailService);
        testUser = new User("testuser", "password", "Test User", "test@example.com");
    }

    @Test
    @DisplayName("Should register and remove observer correctly")
    void testRegisterAndRemoveObserver() {
        notificationService.setTestMode(false);
        
        Observer mockObserver = mock(Observer.class);
        notificationService.registerObserver(mockObserver);

        NotificationMessage message = new NotificationMessage("Hello");
        notificationService.notifyObservers(testUser, message);

        verify(emailService, times(1)).sendEmail(eq("test@example.com"), anyString(), eq("Hello"));

        verify(mockObserver, times(1)).notify(eq(testUser), eq(message.getContent()));

        notificationService.removeObserver(mockObserver);

        NotificationMessage message2 = new NotificationMessage("Second");
        notificationService.notifyObservers(testUser, message2);

        verifyNoMoreInteractions(mockObserver);
    }

    @Test
    @DisplayName("Should record messages in test mode without sending actual emails")
    void testTestMode() {
        notificationService.setTestMode(true);
        
        NotificationMessage message = new NotificationMessage("Test message");
        
        notificationService.notifyObservers(testUser, message);
        
        assertEquals(1, notificationService.getSentMessages().size(), 
            "Should have 1 message in sentMessages");
        
        String sentMessage = notificationService.getSentMessages().get(0);
        assertTrue(sentMessage.contains("Test User"), 
            "Message should contain user name. Actual: " + sentMessage);
        assertTrue(sentMessage.contains("Test message"), 
            "Message should contain the test content. Actual: " + sentMessage);
        
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}