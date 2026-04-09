package com.appointments.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.appointments.persistence.InMemoryRepository;
import com.appointments.service.*;
import com.appointments.domain.*;

@DisplayName("SchedulingService Tests")
class SchedulingServiceTest {

    private SchedulingService service;
    private InMemoryRepository repo;
    private NotificationService notificationService;
    private User user;
    private TimeSlot testSlot;

    @BeforeEach
    void setup() {
        repo = mock(InMemoryRepository.class);
        notificationService = mock(NotificationService.class);
        service = new SchedulingService(repo, notificationService);

        user = new User("user1", "1234", "Test User", "test@test.com");
        testSlot = new TimeSlot(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1)
        );
    }

    // ==================== اختبارات viewAvailableSlots ====================

    @Test
    @DisplayName("Should return available slots")
    void testViewAvailableSlots() {
        List<TimeSlot> expectedSlots = List.of(testSlot);
        when(repo.getAvailableSlots()).thenReturn(expectedSlots);

        List<TimeSlot> slots = service.viewAvailableSlots();

        assertEquals(1, slots.size());
        assertEquals(testSlot, slots.get(0));
        verify(repo, times(1)).getAvailableSlots();
    }

    @Test
    @DisplayName("Should return empty list when no slots available")
    void testViewAvailableSlotsEmpty() {
        when(repo.getAvailableSlots()).thenReturn(new ArrayList<>());

        List<TimeSlot> slots = service.viewAvailableSlots();

        assertNotNull(slots);
        assertTrue(slots.isEmpty());
        verify(repo, times(1)).getAvailableSlots();
    }

    // ==================== اختبارات bookAppointment ====================

    @Test
    @DisplayName("Should book appointment successfully")
    void testBookAppointmentSuccess() {
        when(repo.getAvailableSlots()).thenReturn(List.of(testSlot));
        testSlot.setAvailable(true);
        
        List<BookingRuleStrategy> rules = new ArrayList<>();

        assertDoesNotThrow(() -> {
            service.bookAppointment(user, testSlot, AppointmentType.INDIVIDUAL, rules);
        });

        assertFalse(testSlot.isAvailable());
        verify(repo, times(1)).saveAppointment(any(Appointment.class));
        verify(notificationService, times(1)).notifyObservers(eq(user), any(NotificationMessage.class));
    }

    @Test
    @DisplayName("Should throw exception when booking with unavailable slot")
    void testBookAppointmentFailWhenSlotNotAvailable() {
        testSlot.setAvailable(false);

        assertThrows(IllegalArgumentException.class, () -> {
            service.bookAppointment(user, testSlot, AppointmentType.INDIVIDUAL, new ArrayList<>());
        });
        
        verify(repo, never()).saveAppointment(any(Appointment.class));
    }

    

    @Test
    @DisplayName("Should throw exception when booking with null slot")
    void testBookAppointmentNullSlot() {
        assertThrows(NullPointerException.class, () -> {
            service.bookAppointment(user, null, AppointmentType.INDIVIDUAL, new ArrayList<>());
        });
    }

    @Test
    @DisplayName("Should book appointment with different appointment types")
    void testBookAppointmentDifferentTypes() {
        when(repo.getAvailableSlots()).thenReturn(List.of(testSlot));
        testSlot.setAvailable(true);
        List<BookingRuleStrategy> rules = new ArrayList<>();

        assertDoesNotThrow(() -> {
            service.bookAppointment(user, testSlot, AppointmentType.GROUP, rules);
        });
        
        assertFalse(testSlot.isAvailable());
    }

    // ==================== اختبارات modifyAppointment ====================

    @Test
    @DisplayName("Should modify appointment successfully")
    void testModifyAppointment() {
        Appointment appointment = mock(Appointment.class);
        TimeSlot oldSlot = mock(TimeSlot.class);
        when(oldSlot.getStart()).thenReturn(LocalDateTime.now().plusDays(1));
        when(appointment.getTimeSlot()).thenReturn(oldSlot);
        when(appointment.getUser()).thenReturn(user);
        when(repo.findAppointment("app-id")).thenReturn(appointment);
        
        TimeSlot newSlot = mock(TimeSlot.class);
        when(newSlot.isAvailable()).thenReturn(true);
        doNothing().when(newSlot).setAvailable(false);

        service.modifyAppointment("app-id", newSlot);

        verify(oldSlot).setAvailable(true);
        verify(appointment).setTimeSlot(newSlot);
        verify(newSlot).setAvailable(false);
        verify(notificationService, times(1)).notifyObservers(eq(user), any(NotificationMessage.class));
    }

    @Test
    @DisplayName("Should not modify non-existent appointment")
    void testModifyNonExistentAppointment() {
        when(repo.findAppointment("non-existent-id")).thenReturn(null);
        
        TimeSlot newSlot = mock(TimeSlot.class);
        when(newSlot.isAvailable()).thenReturn(true);

        service.modifyAppointment("non-existent-id", newSlot);

        verify(newSlot, never()).setAvailable(false);
        verify(repo, never()).saveAppointment(any());
    }

    @Test
    @DisplayName("Should not modify past appointment")
    void testModifyPastAppointment() {
        Appointment appointment = mock(Appointment.class);
        TimeSlot pastSlot = mock(TimeSlot.class);
        when(pastSlot.getStart()).thenReturn(LocalDateTime.now().minusDays(1));
        when(appointment.getTimeSlot()).thenReturn(pastSlot);
        when(repo.findAppointment("app-id")).thenReturn(appointment);
        
        TimeSlot newSlot = mock(TimeSlot.class);
        when(newSlot.isAvailable()).thenReturn(true);

        service.modifyAppointment("app-id", newSlot);

        verify(pastSlot, never()).setAvailable(anyBoolean());
        verify(appointment, never()).setTimeSlot(any());
        verify(newSlot, never()).setAvailable(false);
    }

    // ==================== اختبارات cancelAppointment ====================

    @Test
    @DisplayName("Should cancel appointment successfully")
    void testCancelAppointment() {
        Appointment appointment = mock(Appointment.class);
        TimeSlot slot = mock(TimeSlot.class);
        when(slot.getStart()).thenReturn(LocalDateTime.now().plusDays(1));
        when(appointment.getTimeSlot()).thenReturn(slot);
        when(appointment.getUser()).thenReturn(user);
        when(repo.findAppointment("app-id")).thenReturn(appointment);

        service.cancelAppointment("app-id");

        verify(slot).setAvailable(true);
        verify(repo).removeAppointment("app-id");
        verify(notificationService, times(1)).notifyObservers(eq(user), any(NotificationMessage.class));
    }

    @Test
    @DisplayName("Should not cancel non-existent appointment")
    void testCancelNonExistentAppointment() {
        when(repo.findAppointment("non-existent-id")).thenReturn(null);

        service.cancelAppointment("non-existent-id");

        verify(repo, never()).removeAppointment(anyString());
        verify(notificationService, never()).notifyObservers(any(), any());
    }

    @Test
    @DisplayName("Should not cancel past appointment")
    void testCancelPastAppointment() {
        Appointment appointment = mock(Appointment.class);
        TimeSlot pastSlot = mock(TimeSlot.class);
        when(pastSlot.getStart()).thenReturn(LocalDateTime.now().minusDays(1));
        when(appointment.getTimeSlot()).thenReturn(pastSlot);
        when(repo.findAppointment("app-id")).thenReturn(appointment);

        service.cancelAppointment("app-id");

        verify(pastSlot, never()).setAvailable(anyBoolean());
        verify(repo, never()).removeAppointment(anyString());
    }

    // ==================== اختبارات notificationService ====================

    @Test
    @DisplayName("Should work without notification service")
    void testWithoutNotificationService() {
        SchedulingService serviceWithoutNotif = new SchedulingService(repo, null);
        when(repo.getAvailableSlots()).thenReturn(List.of(testSlot));
        testSlot.setAvailable(true);
        
        assertDoesNotThrow(() -> {
            serviceWithoutNotif.bookAppointment(user, testSlot, AppointmentType.INDIVIDUAL, new ArrayList<>());
        });
        
        assertFalse(testSlot.isAvailable());
    }

    @Test
    @DisplayName("Should handle modify appointment with null notification service")
    void testModifyWithNullNotification() {
        SchedulingService serviceWithoutNotif = new SchedulingService(repo, null);
        Appointment appointment = mock(Appointment.class);
        TimeSlot oldSlot = mock(TimeSlot.class);
        when(oldSlot.getStart()).thenReturn(LocalDateTime.now().plusDays(1));
        when(appointment.getTimeSlot()).thenReturn(oldSlot);
        when(repo.findAppointment("app-id")).thenReturn(appointment);
        
        TimeSlot newSlot = mock(TimeSlot.class);
        when(newSlot.isAvailable()).thenReturn(true);
        doNothing().when(newSlot).setAvailable(false);

        assertDoesNotThrow(() -> {
            serviceWithoutNotif.modifyAppointment("app-id", newSlot);
        });
         
        verify(oldSlot).setAvailable(true);
        verify(appointment).setTimeSlot(newSlot);
        verify(newSlot).setAvailable(false);
    }
}