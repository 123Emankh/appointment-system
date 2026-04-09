package com.appointments.persistence;

import com.appointments.domain.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the repository.
 * <p>
 * This class simulates a data storage layer using collections instead of a database.
 * It manages users, administrators, appointments, and available time slots.
 * </p>
 *
 * @author Eman
 * @version 1.0
 */
public class InMemoryRepository {

    private Map<String, User> users;
    private Map<String, Administrator> admins;
    private Map<String, Appointment> appointments;
    private List<TimeSlot> availableSlots;

    /**
     * Constructs the repository and initializes sample data.
     */
    public InMemoryRepository() {
        this.users = new HashMap<>();
        this.admins = new HashMap<>();
        this.appointments = new HashMap<>();
        this.availableSlots = new ArrayList<>();

        initializeSampleData();
    }

    /**
     * Initializes sample administrators, users, and available time slots.
     */
    private void initializeSampleData() {
        // Admin
        Administrator admin = new Administrator("admin", "1234", "System Admin");
        saveAdministrator(admin);

        // User
        User user = new User("eman", "1234", "User One", "ekh9951@gmail.com");
        saveUser(user);  
        User user1 = new User("user1", "password", "User One", "user@example.com");
        user1.setPhoneNumber("+1234567890");
        saveUser(user1);

        // Sample slots
        LocalDateTime now = LocalDateTime.now();
        addAvailableTimeSlot(new TimeSlot(now.plusDays(1).withHour(10), now.plusDays(1).withHour(11)));
        addAvailableTimeSlot(new TimeSlot(now.plusDays(1).withHour(11), now.plusDays(1).withHour(12)));
        addAvailableTimeSlot(new TimeSlot(now.plusDays(1).withHour(12), now.plusDays(1).withHour(13)));
    }
    /**
     * Clears all data from the repository for testing purposes.
     */
    public void clearAllData() {
        users.clear();
        admins.clear();
        appointments.clear();
        availableSlots.clear();
    }
    // ===================== User operations =====================

    /**
     * Saves a user in the repository.
     *
     * @param user the user to save
     */
    public void saveUser(User user) {
        users.put(user.getUsername(), user);
    }

    /**
     * Finds a user by username.
     *
     * @param username the username to search for
     * @return the user if found, otherwise null
     */
    public User findUser(String username) {
        return users.get(username);
    }

    /**
     * Returns all users in the repository.
     *
     * @return list of users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // ===================== Admin operations =====================

    /**
     * Saves an administrator in the repository.
     *
     * @param admin the administrator to save
     */
    public void saveAdministrator(Administrator admin) {
        admins.put(admin.getAdminId(), admin);
    }

    /**
     * Finds an administrator by ID.
     *
     * @param adminId the administrator ID
     * @return the administrator if found, otherwise null
     */
    public Administrator findAdministrator(String adminId) {
        return admins.get(adminId);
    }

    // ===================== Appointment operations =====================

    /**
     * Saves an appointment in the repository.
     *
     * @param appointment the appointment to save
     */
    public void saveAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }

    /**
     * Finds an appointment by ID.
     *
     * @param id the appointment ID
     * @return the appointment if found, otherwise null
     */
    public Appointment findAppointment(String id) {
        return appointments.get(id);
    }

    /**
     * Removes an appointment from the repository.
     *
     * @param id the appointment ID to remove
     */
    public void removeAppointment(String id) {
        appointments.remove(id);
    }

    /**
     * Returns all appointments.
     *
     * @return list of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }

    /**
     * Returns all appointments for a specific user.
     *
     * @param user the user whose appointments are requested
     * @return list of appointments belonging to the user
     */
    public List<Appointment> getUserAppointments(User user) {
        return appointments.values().stream()
            .filter(a -> a.getUser().equals(user))
            .collect(Collectors.toList());
    }

    // ===================== TimeSlot operations =====================

    /**
     * Adds a new available time slot.
     *
     * @param slot the time slot to add
     */
    public void addAvailableTimeSlot(TimeSlot slot) {
        availableSlots.add(slot);
    }

    /**
     * Returns all available (unbooked) time slots.
     *
     * @return list of available time slots
     */
    public List<TimeSlot> getAvailableSlots() {
        return availableSlots.stream()
            .filter(TimeSlot::isAvailable)
            .collect(Collectors.toList());
    }

    /**
     * Removes a time slot from the repository.
     *
     * @param slot the time slot to remove
     */
    public void removeTimeSlot(TimeSlot slot) {
        availableSlots.remove(slot);
    }

    /**
     * Checks whether a given time slot overlaps with existing available slots.
     *
     * @param newSlot the new time slot to check
     * @return true if overlapping exists, false otherwise
     */
    public boolean isSlotOverlapping(TimeSlot newSlot) {
        return availableSlots.stream()
            .anyMatch(slot -> slot.isAvailable() && slot.overlaps(newSlot));
    }
}