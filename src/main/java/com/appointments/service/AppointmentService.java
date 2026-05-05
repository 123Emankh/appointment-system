package com.appointments.service;

import com.appointments.domain.*;
import com.appointments.persistence.InMemoryRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing appointments.
 * <p>
 * It handles booking, modification, cancellation, and validation of appointments
 * using a set of booking rules and integrates with the notification system.
 * </p>
 *
 * @author Eman
 * @version 1.0
 */
public class AppointmentService {

    private InMemoryRepository repository;
    private NotificationService notificationService;
    private List<BookingRuleStrategy> bookingRules;

    /**
     * Constructs the AppointmentService.
     *
     * @param repository the data repository
     * @param notificationService the notification service
     */
    public AppointmentService(InMemoryRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
        this.bookingRules = new ArrayList<>();
    }

    /**
     * Adds a booking validation rule.
     *
     * @param rule the booking rule to add
     */
    public void addBookingRule(BookingRuleStrategy rule) {
        bookingRules.add(rule);
    }

    /**
     * Returns all available time slots.
     *
     * @return list of available slots
     */
    public List<TimeSlot> getAvailableSlots() {
        return repository.getAvailableSlots();
    }

    /**
     * Adds a new available time slot.
     *
     * @param slot the time slot to add
     */
    public void addAvailableSlot(TimeSlot slot) {
        repository.addAvailableTimeSlot(slot);
    }

    /**
     * Books an appointment after validating all rules.
     *
     * @param appointment the appointment to book
     * @throws IllegalArgumentException if any booking rule is violated
     */
    public void bookAppointment(Appointment appointment) {
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

        String msg = " reminder of your appointment at" + slot.getStart();
        notificationService.notifyObservers(appointment.getUser(), new NotificationMessage(msg));
    }

    /**
     * Modifies an existing appointment to a new time slot.
     *
     * @param appointmentId the appointment ID
     * @param newSlot the new time slot
     * @throws IllegalArgumentException if appointment is not found or invalid
     */
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

        appointment.getTimeSlot().setAvailable(true);
        appointment.setTimeSlot(newSlot);
        newSlot.setAvailable(false);

        String msg = "Your appointment has been changed to " + newSlot.getStart();
        notificationService.notifyObservers(appointment.getUser(), new NotificationMessage(msg));
    }

    /**
     * Cancels an appointment.
     *
     * @param appointmentId the appointment ID
     * @param requester the user requesting cancellation
     */
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

        appointment.getTimeSlot().setAvailable(true);
        repository.removeAppointment(appointmentId);

        String msg = "Cancel your appointment  ";
        notificationService.notifyObservers(appointment.getUser(), new NotificationMessage(msg));
    }
    /**
     * Retrieves all appointments from the repository.
     *
     * @return a list of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return repository.getAllAppointments();
    }
    /**
     * Retrieves all appointments belonging to a specific user.
     *
     * @param user the user whose appointments to retrieve
     * @return a list of appointments for the given user
     */
    public List<Appointment> getUserAppointments(User user) {
        return repository.getUserAppointments(user);
    }

    /**
     * Finds an appointment by its unique identifier.
     *
     * @param id the appointment ID
     * @return the appointment if found, null otherwise
     */
    public Appointment findAppointment(String id) {
        return repository.findAppointment(id);
    }
}