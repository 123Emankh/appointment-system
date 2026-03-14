package com.appointments.service;

import com.appointments.domain.Appointment;

/**
 * واجهة لقواعد الحجز (Strategy Pattern).
 * @author فريقك
 * @version 1.0
 */
public interface BookingRuleStrategy {
    /**
     * تحقق الصحة.
     * @param appointment الموعد
     * @return true إذا صالح
     */
    boolean isValid(Appointment appointment);
}