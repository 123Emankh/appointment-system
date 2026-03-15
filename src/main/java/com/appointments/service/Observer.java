package com.appointments.service;

import com.appointments.domain.User;

public interface Observer {
    void notify(User user, String message);
}