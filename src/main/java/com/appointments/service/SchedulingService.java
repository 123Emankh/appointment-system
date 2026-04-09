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
 * Service class that handles scheduling operations including viewing available slots,
 * booking, modifying, and cancelling appointments.
 * This service uses an in-memory repository and a notification service.
 *
 * @author Eman
 * @version 1.0
 */
public class SchedulingService {
    private InMemoryRepository repo;
    private NotificationService notificationService;

    /**
     * Constructs a SchedulingService with the given repository and notification service.
     *
     * @param repo                the in-memory repository for data access
     * @param notificationService the notification service for sending alerts
     */
    public SchedulingService(InMemoryRepository repo, NotificationService notificationService) {
        this.repo = repo;
        this.notificationService = notificationService;
    }

    /**
     * Returns a list of all currently available time slots.
     *
     * @return list of available TimeSlot objects
     */
    public List<TimeSlot> viewAvailableSlots() {
        return repo.getAvailableSlots();
    }

    /**
     * Books an appointment for a user after validating all booking rules.
     *
     * @param user      the user booking the appointment
     * @param timeSlot  the desired time slot
     * @param type      the type of appointment
     * @param rules     list of booking rules to validate against
     * @throws IllegalArgumentException if any rule fails or the slot is not available
     */
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
                throw new IllegalArgumentException(" Rule violation:: " + rule.getClass().getSimpleName());
            }
        }

        if (!timeSlot.isAvailable()) {
            throw new IllegalArgumentException(" The slot is unavailable ");
        }

        timeSlot.setAvailable(false);
        repo.saveAppointment(appointment);

        if (notificationService != null) {
            String msg = "Reminder of your appointment at" + timeSlot.getStart();
            notificationService.notifyObservers(user, new NotificationMessage(msg));
        }
    }

    /**
     * Modifies an existing appointment to a new time slot.
     * Only future appointments can be modified.
     *
     * @param appId   the ID of the appointment to modify
     * @param newSlot the new time slot for the appointment
     */
    public void modifyAppointment(String appId, TimeSlot newSlot) {
        Appointment app = repo.findAppointment(appId);
        if (app != null && app.getTimeSlot().getStart().isAfter(LocalDateTime.now())) {
            app.getTimeSlot().setAvailable(true);
            app.setTimeSlot(newSlot);
            newSlot.setAvailable(false);

            if (notificationService != null) {
                String msg = "Your appointment has been changed to " + newSlot.getStart();
                notificationService.notifyObservers(app.getUser(), new NotificationMessage(msg));
            }
        }
    }

    /**
     * Cancels an existing appointment.
     * Only future appointments can be cancelled.
     *
     * @param appId the ID of the appointment to cancel
     */
    public void cancelAppointment(String appId) {
        Appointment app = repo.findAppointment(appId);
        if (app != null && app.getTimeSlot().getStart().isAfter(LocalDateTime.now())) {
            app.getTimeSlot().setAvailable(true);
            repo.removeAppointment(appId);

            if (notificationService != null) {
                String msg = "Your appointment has been cancelled";
                notificationService.notifyObservers(app.getUser(), new NotificationMessage(msg));
            }
        }
    }
}