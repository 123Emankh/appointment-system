package com.appointments.service;

import com.appointments.domain.*;
import com.appointments.persistence.InMemoryRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private InMemoryRepository repository;
    private NotificationService notificationService;
    private List<BookingRuleStrategy> bookingRules;

    public AppointmentService(InMemoryRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
        this.bookingRules = new ArrayList<>();
    }

    public void addBookingRule(BookingRuleStrategy rule) {
        bookingRules.add(rule);
    }

    public List<TimeSlot> getAvailableSlots() {
        return repository.getAvailableSlots();
    }

    public void addAvailableSlot(TimeSlot slot) {
        repository.addAvailableTimeSlot(slot);
    }

    public void bookAppointment(Appointment appointment) {
        // تطبيق القواعد
        for (BookingRuleStrategy rule : bookingRules) {
            if (!rule.isValid(appointment)) {
                throw new IllegalArgumentException("Booking rule violated: " + rule.getClass().getSimpleName());
            }
        }

        TimeSlot slot = appointment.getTimeSlot();
        if (!slot.isAvailable()) {
            throw new IllegalArgumentException("Time slot is not available.");
        }

        slot.setAvailable(false);
        repository.saveAppointment(appointment);

        // إرسال إشعار
        String msg = "تذكير بموعدك في " + slot.getStart();
        notificationService.notifyObservers(appointment.getUser(), new NotificationMessage(msg));
    }

    public void modifyAppointment(String appointmentId, TimeSlot newSlot) {
        Appointment appointment = repository.findAppointment(appointmentId);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found.");
        }
        if (!appointment.isFuture()) {
            throw new IllegalArgumentException("Cannot modify past appointments.");
        }
        if (!newSlot.isAvailable()) {
            throw new IllegalArgumentException("New time slot is not available.");
        }

        // إعادة الفتحة القديمة للحالة متاحة
        appointment.getTimeSlot().setAvailable(true);
        // تعيين الفتحة الجديدة
        appointment.setTimeSlot(newSlot);
        newSlot.setAvailable(false);

        // إرسال إشعار
        String msg = "تم تعديل موعدك إلى " + newSlot.getStart();
        notificationService.notifyObservers(appointment.getUser(), new NotificationMessage(msg));
    }

    public void cancelAppointment(String appointmentId, User requester) {
        Appointment appointment = repository.findAppointment(appointmentId);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found.");
        }
        if (!appointment.isFuture()) {
            throw new IllegalArgumentException("Cannot cancel past appointments.");
        }
        if (requester != null && !appointment.getUser().equals(requester)) {
            throw new IllegalArgumentException("You can only cancel your own appointments.");
        }

        // إعادة الفتحة متاحة
        appointment.getTimeSlot().setAvailable(true);
        repository.removeAppointment(appointmentId);

        // إرسال إشعار
        String msg = "تم إلغاء موعدك";
        notificationService.notifyObservers(appointment.getUser(), new NotificationMessage(msg));
    }

    public List<Appointment> getAllAppointments() {
        return repository.getAllAppointments();
    }

    public List<Appointment> getUserAppointments(User user) {
        return repository.getUserAppointments(user);
    }

    public Appointment findAppointment(String id) {
        return repository.findAppointment(id);
    }
}