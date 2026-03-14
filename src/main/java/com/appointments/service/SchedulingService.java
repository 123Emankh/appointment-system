package com.appointments.service;

import com.appointments.persistence.InMemoryRepository;
import com.appointments.domain.Appointment;
import com.appointments.domain.AppointmentType;
import com.appointments.domain.TimeSlot;
import com.appointments.domain.User;
import com.appointments.domain.NotificationMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * خدمة الجدولة.
 * @author فريقك
 * @version 1.0
 */
public class SchedulingService {
    private InMemoryRepository repo;
    private NotificationService notificationService;

    public SchedulingService(InMemoryRepository repo, NotificationService notificationService) {
        this.repo = repo;
        this.notificationService = notificationService;
    }

    /**
     * عرض الفتحات المتاحة.
     */
    public List<TimeSlot> viewAvailableSlots() {
        // استخدم getAvailableSlots مباشرة من repo (بدون getSchedule)
        return repo.getAvailableSlots();
    }

    /**
     * حجز موعد.
     * @param user المستخدم الذي يحجز
     * @param timeSlot الفتحة المطلوبة
     * @param type نوع الموعد
     * @param rules قواعد التحقق
     */
    public void bookAppointment(User user, TimeSlot timeSlot, AppointmentType type, List<BookingRuleStrategy> rules) {
        // إنشاء كائن Appointment باستخدام الكونستركتور الموجود
        Appointment appointment = new Appointment(
            UUID.randomUUID().toString(),
            user,
            timeSlot,
            type,
            "Confirmed"
        );

        // التحقق من القواعد
        for (BookingRuleStrategy rule : rules) {
            if (!rule.isValid(appointment)) {
                throw new IllegalArgumentException("انتهاك قاعدة: " + rule.getClass().getSimpleName());
            }
        }

        // التحقق من أن الفتحة متاحة
        if (!timeSlot.isAvailable()) {
            throw new IllegalArgumentException("الفتحة غير متاحة");
        }

        // تحديث حالة الفتحة إلى محجوزة
        timeSlot.setAvailable(false);

        // حفظ الموعد في المستودع (استخدم saveAppointment بدلاً من addAppointment)
        repo.saveAppointment(appointment);

        // إرسال إشعار
        if (notificationService != null) {
            String msg = "تذكير بموعدك في " + timeSlot.getStart();
            notificationService.notifyObservers(user, new NotificationMessage(msg));
        }
    }

    /**
     * تعديل موعد.
     * @param appId معرف الموعد
     * @param newSlot الفتحة الجديدة
     */
    public void modifyAppointment(String appId, TimeSlot newSlot) {
        Appointment app = repo.findAppointment(appId);
        if (app != null && app.getTimeSlot().getStart().isAfter(LocalDateTime.now())) {
            // إعادة الفتحة القديمة إلى متاحة
            app.getTimeSlot().setAvailable(true);
            
            // تحديث الفتحة الجديدة
            app.setTimeSlot(newSlot);
            newSlot.setAvailable(false);
            
            // لا حاجة لإنشاء كائن جديد، فقط تعديل الموجود
            // يمكن استدعاء repo.saveAppointment(app) إذا أردت التأكد من التحديث
            
            // إرسال إشعار بالتعديل
            if (notificationService != null) {
                String msg = "تم تعديل موعدك إلى " + newSlot.getStart();
                notificationService.notifyObservers(app.getUser(), new NotificationMessage(msg));
            }
        }
    }

    /**
     * إلغاء موعد.
     * @param appId معرف الموعد
     */
    public void cancelAppointment(String appId) {
        Appointment app = repo.findAppointment(appId);
        if (app != null && app.getTimeSlot().getStart().isAfter(LocalDateTime.now())) {
            // جعل الفتحة متاحة مرة أخرى
            app.getTimeSlot().setAvailable(true);
            
            // إزالة الموعد من المستودع
            repo.removeAppointment(appId);
            
            // إرسال إشعار بالإلغاء
            if (notificationService != null) {
                String msg = "تم إلغاء موعدك";
                notificationService.notifyObservers(app.getUser(), new NotificationMessage(msg));
            }
        }
    }
}