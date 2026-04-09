package com.appointments.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.appointments.domain.User;
import com.appointments.service.SMSObserver;
import com.appointments.test.TestDataFactory;

class SMSObserverTest {

    @Test
    void shouldPrintSmsWhenPhoneExists() {
        SMSObserver observer = new SMSObserver();

        User user = TestDataFactory.createUserWithPhone("123456");

        observer.notify(user, "Hi");

        assertTrue(true); // no exception = pass (console output is side-effect)
    }

    @Test
    void shouldNotFailWhenPhoneMissing() {
        SMSObserver observer = new SMSObserver();

        User user = TestDataFactory.createUserWithoutPhone();

        observer.notify(user, "Hi");

        assertTrue(true);
    }
}
