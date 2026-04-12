package com.appointments.test;


import com.appointments.domain.*;
import com.appointments.service.BookingRuleStrategy;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class FutureDateRule implements BookingRuleStrategy {
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.isFuture();
    }
}

class BookingRuleStrategyTest {

    @Test
    void futureDateRule_validFutureAppointment_returnsTrue() {
        User user = new User("u1", "p", "N", "e@e.com");
        TimeSlot futureSlot = new TimeSlot(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        Appointment futureApp = new Appointment("a1", user, futureSlot, AppointmentType.INDIVIDUAL, "SCHEDULED");
        BookingRuleStrategy rule = new FutureDateRule();
        assertTrue(rule.isValid(futureApp));
    }

    @Test
    void futureDateRule_pastAppointment_returnsFalse() {
        User user = new User("u1", "p", "N", "e@e.com");
        TimeSlot pastSlot = new TimeSlot(LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1));
        Appointment pastApp = new Appointment("a2", user, pastSlot, AppointmentType.INDIVIDUAL, "SCHEDULED");
        BookingRuleStrategy rule = new FutureDateRule();
        assertFalse(rule.isValid(pastApp));
    }
}
