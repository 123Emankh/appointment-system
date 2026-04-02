package com.appointments.service;

import com.appointments.domain.Appointment;

public interface BookingRuleStrategy {
    boolean isValid(Appointment appointment);
}