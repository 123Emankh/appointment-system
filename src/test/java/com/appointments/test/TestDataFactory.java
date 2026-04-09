package com.appointments.test;

import com.appointments.domain.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestDataFactory {

    public static Appointment createAppointmentWithDuration(int duration) {
        Appointment app = mock(Appointment.class);
        when(app.getDurationMinutes()).thenReturn((long) duration);
        return app;
    }

    public static Appointment createAppointmentWithParticipants(int count, AppointmentType type) {
        Appointment app = mock(Appointment.class);

        List<User> participants = java.util.stream.IntStream.range(0, count)
                .mapToObj(i -> mock(User.class))
                .toList();

        when(app.getParticipants()).thenReturn(participants);
        when(app.getType()).thenReturn(type);

        return app;
    }

    public static Appointment createAppointmentWithStart(LocalDateTime start, AppointmentType type) {
        Appointment app = mock(Appointment.class);
        TimeSlot slot = mock(TimeSlot.class);

        when(slot.getStart()).thenReturn(start);
        when(app.getTimeSlot()).thenReturn(slot);
        when(app.getType()).thenReturn(type);

        return app;
    }

    public static User createUserWithEmail(String email) {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn(email);
        when(user.getName()).thenReturn("Test User");
        return user;
    }

    public static User createUserWithoutEmail() {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn(null);
        return user;
    }

    public static User createUserWithPhone(String phone) {
        User user = mock(User.class);
        when(user.getPhoneNumber()).thenReturn(phone);
        return user;
    }

    public static User createUserWithoutPhone() {
        User user = mock(User.class);
        when(user.getPhoneNumber()).thenReturn(null);
        return user;
    }

    public static TimeSlot createAvailableSlot() {
        TimeSlot slot = mock(TimeSlot.class);
        when(slot.isAvailable()).thenReturn(true);
        when(slot.getStart()).thenReturn(LocalDateTime.now().plusHours(1));
        return slot;
    }
}