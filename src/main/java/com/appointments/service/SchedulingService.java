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

public class SchedulingService {
    private InMemoryRepository repo;
    private NotificationService notificationService;

    public SchedulingService(InMemoryRepository repo, NotificationService notificationService) {
        this.repo = repo;
        this.notificationService = notificationService;
    }

    public List<TimeSlot> viewAvailableSlots() {
        return repo.getAvailableSlots();
    }

    public void bookAppointment(User user, TimeSlot timeSlot, AppointmentType type, List<BookingRuleStrategy> rules) {
        Appointment appointment = new Appointment(
            UUID.randomUUID().toString(),
            user,
            timeSlot,
            type,
            "Confirmed"
        );

        for (BookingRuleStrategy rule : rules) {
            if (!rule.isValid(appointment)) {
                throw new IllegalArgumentException("انتهاك قاعدة: " + rule.getClass().getSimpleName());
            }
        }

        if (!timeSlot.isAvailable()) {
            throw new IllegalArgumentException("الفتحة غير متاحة");
        }

        timeSlot.setAvailable(false);
        repo.saveAppointment(appointment);

        if (notificationService != null) {
            String msg = "تذكير بموعدك في " + timeSlot.getStart();
            notificationService.notifyObservers(user, new NotificationMessage(msg));
        }
    }

    public void modifyAppointment(String appId, TimeSlot newSlot) {
        Appointment app = repo.findAppointment(appId);
        if (app != null && app.getTimeSlot().getStart().isAfter(LocalDateTime.now())) {
            app.getTimeSlot().setAvailable(true);
            app.setTimeSlot(newSlot);
            newSlot.setAvailable(false);

            if (notificationService != null) {
                String msg = "تم تعديل موعدك إلى " + newSlot.getStart();
                notificationService.notifyObservers(app.getUser(), new NotificationMessage(msg));
            }
        }
    }

    public void cancelAppointment(String appId) {
        Appointment app = repo.findAppointment(appId);
        if (app != null && app.getTimeSlot().getStart().isAfter(LocalDateTime.now())) {
            app.getTimeSlot().setAvailable(true);
            repo.removeAppointment(appId);

            if (notificationService != null) {
                String msg = "تم إلغاء موعدك";
                notificationService.notifyObservers(app.getUser(), new NotificationMessage(msg));
            }
        }
    }
}