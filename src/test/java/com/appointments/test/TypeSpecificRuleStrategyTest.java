package com.appointments.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.appointments.domain.Appointment;
import com.appointments.domain.AppointmentType;
import com.appointments.test.TestDataFactory;
import com.appointments.service.TypeSpecificRuleStrategy;

import java.time.LocalDateTime;

class TypeSpecificRuleStrategyTest {

    @Test
    void urgentShouldPassWithin24Hours() {
        TypeSpecificRuleStrategy rule = new TypeSpecificRuleStrategy();

        Appointment app = TestDataFactory.createAppointmentWithStart(
                LocalDateTime.now().plusHours(10),
                AppointmentType.URGENT
        );

        assertTrue(rule.isValid(app));
    }

    @Test
    void urgentShouldFailBeyond24Hours() {
        TypeSpecificRuleStrategy rule = new TypeSpecificRuleStrategy();

        Appointment app = TestDataFactory.createAppointmentWithStart(
                LocalDateTime.now().plusHours(30),
                AppointmentType.URGENT
        );

        assertFalse(rule.isValid(app));
    }

    @Test
    void assessmentOutsideBusinessHoursShouldFail() {
        TypeSpecificRuleStrategy rule = new TypeSpecificRuleStrategy();

        Appointment app = TestDataFactory.createAppointmentWithStart(
                LocalDateTime.now().withHour(20),
                AppointmentType.ASSESSMENT
        );

        assertFalse(rule.isValid(app));
    }
}