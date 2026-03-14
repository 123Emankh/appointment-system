package com.appointments.service;

import com.appointments.domain.Appointment;
import com.appointments.domain.AppointmentType;

/**
 * قاعدة حسب النوع.
 * @author فريقك
 * @version 1.0
 */
public class TypeSpecificRuleStrategy implements BookingRuleStrategy {
    @Override
    public boolean isValid(Appointment appointment) {
        // مثال: urgent يسمح مدة أقصر
        if (appointment.getType() == AppointmentType.URGENT && appointment.getDurationMinutes() > 30) {
            return false;
        }
        return true;
    }
}