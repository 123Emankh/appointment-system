package com.appointments.persistence;

import com.appointments.domain.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRepository {
    private Map<String, User> users;
    private Map<String, Administrator> admins;
    private Map<String, Appointment> appointments;
    private List<TimeSlot> availableSlots;

    public InMemoryRepository() {
        this.users = new HashMap<>();
        this.admins = new HashMap<>();
        this.appointments = new HashMap<>();
        this.availableSlots = new ArrayList<>();

        initializeSampleData();
    }

    private void initializeSampleData() {
        // Admin
        Administrator admin = new Administrator("admin", "1234", "System Admin");
        saveAdministrator(admin);

        // User
        User user = new User("user1", "password", "User One", "user@example.com");
        user.setPhoneNumber("+1234567890");
        saveUser(user);

        // Sample slots
        LocalDateTime now = LocalDateTime.now();
        addAvailableTimeSlot(new TimeSlot(now.plusDays(1).withHour(10), now.plusDays(1).withHour(11)));
        addAvailableTimeSlot(new TimeSlot(now.plusDays(1).withHour(11), now.plusDays(1).withHour(12)));
        addAvailableTimeSlot(new TimeSlot(now.plusDays(1).withHour(12), now.plusDays(1).withHour(13)));
    }

    // User operations
    public void saveUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User findUser(String username) {
        return users.get(username);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // Admin operations
    public void saveAdministrator(Administrator admin) {
        admins.put(admin.getAdminId(), admin);
    }

    public Administrator findAdministrator(String adminId) {
        return admins.get(adminId);
    }

    // Appointment operations
    public void saveAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }

    public Appointment findAppointment(String id) {
        return appointments.get(id);
    }

    public void removeAppointment(String id) {
        appointments.remove(id);
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }

    public List<Appointment> getUserAppointments(User user) {
        return appointments.values().stream()
            .filter(a -> a.getUser().equals(user))
            .collect(Collectors.toList());
    }

    // TimeSlot operations
    public void addAvailableTimeSlot(TimeSlot slot) {
        availableSlots.add(slot);
    }

    public List<TimeSlot> getAvailableSlots() {
        return availableSlots.stream()
            .filter(TimeSlot::isAvailable)
            .collect(Collectors.toList());
    }

    public void removeTimeSlot(TimeSlot slot) {
        availableSlots.remove(slot);
    }
}