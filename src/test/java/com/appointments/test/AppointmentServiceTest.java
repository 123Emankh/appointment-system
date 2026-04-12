package com.appointments.test;

import com.appointments.domain.*;
import com.appointments.persistence.InMemoryRepository;
import com.appointments.service.AppointmentService;
import com.appointments.service.NotificationService;
import com.appointments.service.ParticipantLimitStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    private InMemoryRepository repository;
    private NotificationService notificationService;
    private AppointmentService appointmentService;
    private User testUser;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository();
        repository.clearAllData(); 
        
        notificationService = mock(NotificationService.class);
        appointmentService = new AppointmentService(repository, notificationService);
        testUser = new User("testuser", "pass", "Test User", "test@example.com");
        repository.saveUser(testUser);
    }

    private Appointment createAppointment(TimeSlot slot) {
        return new Appointment(UUID.randomUUID().toString(), testUser, slot, AppointmentType.INDIVIDUAL, "SCHEDULED");
    }

    private TimeSlot createSlot(int hour) {
        LocalDateTime now = LocalDateTime.now();
        return new TimeSlot(now.plusDays(1).withHour(hour), now.plusDays(1).withHour(hour + 1));
    }

    @Test
    void testAddAndGetAvailableSlots() {
        TimeSlot slot1 = createSlot(10);
        TimeSlot slot2 = createSlot(11);
        
        appointmentService.addAvailableSlot(slot1);
        appointmentService.addAvailableSlot(slot2);
        
        List<TimeSlot> slots = appointmentService.getAvailableSlots();
        assertEquals(2, slots.size());
    }

    @Test
    void testBookAppointmentSuccess() {
        TimeSlot slot = createSlot(10);
        appointmentService.addAvailableSlot(slot);
        appointmentService.addBookingRule(new ParticipantLimitStrategy());
        
        Appointment appointment = createAppointment(slot);
        
        assertDoesNotThrow(() -> appointmentService.bookAppointment(appointment));
        assertFalse(slot.isAvailable());
        verify(notificationService, times(1)).notifyObservers(any(), any());
    }

    @Test
    void testBookAppointmentSlotNotAvailable() {
        TimeSlot slot = createSlot(10);
        slot.setAvailable(false); 
        Appointment appointment = createAppointment(slot);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> appointmentService.bookAppointment(appointment));
        
        assertEquals("Time slot is not available.", exception.getMessage());
    }

    @Test
    void testModifyAppointmentSuccess() {
        TimeSlot oldSlot = createSlot(10);
        TimeSlot newSlot = createSlot(11);
        
        appointmentService.addAvailableSlot(oldSlot);
        appointmentService.addAvailableSlot(newSlot);
        
        Appointment appointment = createAppointment(oldSlot);
        appointmentService.bookAppointment(appointment);
        
        assertDoesNotThrow(() -> appointmentService.modifyAppointment(appointment.getId(), newSlot));
        assertTrue(oldSlot.isAvailable());
        assertFalse(newSlot.isAvailable());
    }

    @Test
    void testModifyAppointmentNewSlotNotAvailable() {
        TimeSlot oldSlot = createSlot(10);
        TimeSlot newSlot = createSlot(11);
        newSlot.setAvailable(false); 
        
        appointmentService.addAvailableSlot(oldSlot);
        Appointment appointment = createAppointment(oldSlot);
        appointmentService.bookAppointment(appointment);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> appointmentService.modifyAppointment(appointment.getId(), newSlot));
        
        assertEquals("New time slot is not available.", exception.getMessage());
    }

    @Test
    void testCancelAppointmentSuccess() {
        TimeSlot slot = createSlot(10);
        appointmentService.addAvailableSlot(slot);
        
        Appointment appointment = createAppointment(slot);
        appointmentService.bookAppointment(appointment);
        
        assertDoesNotThrow(() -> appointmentService.cancelAppointment(appointment.getId(), testUser));
        assertNull(repository.findAppointment(appointment.getId()));
        assertTrue(slot.isAvailable());
    }

    @Test
    void testGetUserAppointments() {
        TimeSlot slot1 = createSlot(10);
        TimeSlot slot2 = createSlot(11);
        
        appointmentService.addAvailableSlot(slot1);
        appointmentService.addAvailableSlot(slot2);
        
        Appointment app1 = createAppointment(slot1);
        Appointment app2 = createAppointment(slot2);
        
        appointmentService.bookAppointment(app1);
        appointmentService.bookAppointment(app2);
        
        List<Appointment> userApps = appointmentService.getUserAppointments(testUser);
        assertEquals(2, userApps.size());
    }

    @Test
    void testFindAppointment() {
        TimeSlot slot = createSlot(10);
        appointmentService.addAvailableSlot(slot);
        
        Appointment appointment = createAppointment(slot);
        appointmentService.bookAppointment(appointment);
        
        Appointment found = appointmentService.findAppointment(appointment.getId());
        assertNotNull(found);
        assertEquals(appointment.getId(), found.getId());
    }
    @Test
    @DisplayName("Should handle bookAppointment with null appointment")
    void testBookNullAppointment() {
        assertThrows(Exception.class, () -> appointmentService.bookAppointment(null));
    }

    @Test
    @DisplayName("Should handle modifyAppointment with null appointment")
    void testModifyNullAppointment() {
        TimeSlot newSlot = createSlot(10);
        assertThrows(Exception.class, () -> appointmentService.modifyAppointment(null, newSlot));
    }

    @Test
    @DisplayName("Should handle cancelAppointment with null requester for non-existent")
    void testCancelNonExistentWithNull() {
        assertThrows(IllegalArgumentException.class, 
            () -> appointmentService.cancelAppointment("non-existent", null));
    }
}