package com.appointments.service;
import com.appointments.domain.NotificationMessage; 
import com.appointments.domain.*;
import com.appointments.persistence.InMemoryRepository;
import java.util.List;

public class AppointmentService {
    private InMemoryRepository repository;
    private NotificationService notificationService;
    private List<BookingRuleStrategy> bookingRules;

    public AppointmentService(InMemoryRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
        this.bookingRules = new java.util.ArrayList<>();
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

    public boolean bookAppointment(Appointment appointment) {
        for (BookingRuleStrategy rule : bookingRules) {
            if (!rule.isValid(appointment)) {
                return false;
            }
        }

        TimeSlot slot = appointment.getTimeSlot();
        if (!slot.isAvailable()) {
            return false;
        }

        slot.setAvailable(false);
        repository.saveAppointment(appointment);
        
        notificationService.notifyObservers(appointment.getUser(), 
            new NotificationMessage("تذكير بموعدك في " + slot.getStart()));
        
        return true;
    }

    public boolean cancelAppointment(String appointmentId, User user) {
        Appointment appointment = repository.findAppointment(appointmentId);
        
        if (appointment == null) {
            return false;
        }

        if (user != null && !appointment.getUser().equals(user)) {
            return false;
        }

        if (!appointment.isFuture()) {
            return false;
        }

        appointment.setStatus("Cancelled");
        appointment.getTimeSlot().setAvailable(true);
        repository.removeAppointment(appointmentId);
        
        notificationService.notifyObservers(appointment.getUser(), 
            new NotificationMessage("Your appointment has been cancelled"));
        
        return true;
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