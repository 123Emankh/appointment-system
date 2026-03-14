package com.appointments.service;

import com.appointments.domain.User;

/**
 * واجهة المراقب للإشعارات.
 * @author فريقك
 * @version 1.0
 */
public interface Observer {
    /**
     * إرسال إشعار.
     * @param user المستخدم
     * @param message الرسالة
     */
    void notify(User user, String message);
}