


package com.appointments;

import com.appointments.domain.*;
import com.appointments.persistence.InMemoryRepository;
import com.appointments.service.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static AuthenticationService authService;
    private static AppointmentService appointmentService;
    private static NotificationService notificationService;
    private static InMemoryRepository repository;
    private static Scanner scanner;

    public static void main(String[] args) {
        initializeSystem();
        runApplication();
    }

    private static void initializeSystem() {
        scanner = new Scanner(System.in);
        
        // Initialize repository
        repository = new InMemoryRepository();
        
        // Initialize notification service
        notificationService = new NotificationService();
        
        // Initialize authentication service
        authService = new AuthenticationService(repository);
        
        // Initialize appointment service
        appointmentService = new AppointmentService(repository, notificationService);
        
        // Add booking rules
        appointmentService.addBookingRule(new DurationRuleStrategy(0));
        appointmentService.addBookingRule(new ParticipantLimitStrategy());
        appointmentService.addBookingRule(new TypeSpecificRuleStrategy());
    }

    private static void runApplication() {
        System.out.println("=== Appointment Scheduling System ===\n");

        while (true) {
            System.out.println("1. Login as Administrator");
            System.out.println("2. Login as User");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    userLogin();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void adminLogin() {
        System.out.print("Enter admin ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (authService.authenticateAdmin(id, password)) {
            System.out.println("Login successful!\n");
            adminMenu();
        } else {
            System.out.println("Invalid credentials!\n");
        }
    }

    private static void userLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = authService.authenticateUser(username, password);
        if (user != null) {
            System.out.println("Login successful!\n");
            userMenu(user);
        } else {
            System.out.println("Invalid credentials!\n");
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== Administrator Menu ===");
            System.out.println("1. View all appointments");
            System.out.println("2. View available slots");
            System.out.println("3. Add available slot");
            System.out.println("4. Modify appointment");
            System.out.println("5. Cancel appointment");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllAppointments();
                    break;
                case 2:
                    viewAvailableSlots();
                    break;
                case 3:
                    addAvailableSlot();
                    break;
                case 4:
                    modifyAppointment();
                    break;
                case 5:
                    cancelAppointment();
                    break;
                case 6:
                    authService.logout();
                    System.out.println("Logged out\n");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. View available appointments");
            System.out.println("2. Book appointment");
            System.out.println("3. View my appointments");
            System.out.println("4. Modify my appointment");
            System.out.println("5. Cancel my appointment");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAvailableSlots();
                    break;
                case 2:
                    bookAppointment(user);
                    break;
                case 3:
                    viewUserAppointments(user);
                    break;
                case 4:
                    modifyUserAppointment(user);
                    break;
                case 5:
                    cancelUserAppointment(user);
                    break;
                case 6:
                    authService.logout();
                    System.out.println("Logged out\n");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void viewAvailableSlots() {
        List<TimeSlot> slots = appointmentService.getAvailableSlots();
        if (slots.isEmpty()) {
            System.out.println("No available slots.");
        } else {
            System.out.println("\nAvailable appointments:");
            for (int i = 0; i < slots.size(); i++) {
                System.out.println((i + 1) + ". " + slots.get(i).getStart());
            }
        }
    }

    private static void bookAppointment(User user) {
        List<TimeSlot> slots = appointmentService.getAvailableSlots();
        if (slots.isEmpty()) {
            System.out.println("No available slots.");
            return;
        }

        viewAvailableSlots();
        System.out.print("Choose slot number: ");
        int slotIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (slotIndex >= 0 && slotIndex < slots.size()) {
            Appointment appointment = new Appointment(
                UUID.randomUUID().toString(),
                user,
                slots.get(slotIndex),
                AppointmentType.INDIVIDUAL,
                "Confirmed"
            );

            if (appointmentService.bookAppointment(appointment)) {
                System.out.println("Appointment booked successfully!");
            } else {
                System.out.println("Failed to book appointment. Check rules.");
            }
        }
    }

    private static void viewUserAppointments(User user) {
        List<Appointment> appointments = appointmentService.getUserAppointments(user);
        if (appointments.isEmpty()) {
            System.out.println("You have no appointments.");
        } else {
            System.out.println("\nYour appointments:");
            for (Appointment apt : appointments) {
                System.out.println(apt);
            }
        }
    }

    private static void viewAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            System.out.println("\nAll Appointments:");
            for (Appointment apt : appointments) {
                System.out.println(apt);
            }
        }
    }

    private static void addAvailableSlot() {
        System.out.println("Enter new slot details:");
        System.out.print("Year (2026): ");
        int year = scanner.nextInt();
        System.out.print("Month (3): ");
        int month = scanner.nextInt();
        System.out.print("Day: ");
        int day = scanner.nextInt();
        System.out.print("Hour (0-23): ");
        int hour = scanner.nextInt();
        scanner.nextLine();

        LocalDateTime start = LocalDateTime.of(year, month, day, hour, 0);
        LocalDateTime end = start.plusHours(1);

        TimeSlot slot = new TimeSlot(start, end);
        appointmentService.addAvailableSlot(slot);
        System.out.println("Slot added successfully!");
    }

    private static void modifyAppointment() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments to modify.");
            return;
        }

        viewAllAppointments();
        System.out.print("Enter appointment ID to modify: ");
        String id = scanner.nextLine();

        Appointment appointment = appointmentService.findAppointment(id);
        if (appointment != null) {
            List<TimeSlot> slots = appointmentService.getAvailableSlots();
            viewAvailableSlots();
            System.out.print("Choose new slot number: ");
            int slotIndex = scanner.nextInt() - 1;
            scanner.nextLine();

            if (slotIndex >= 0 && slotIndex < slots.size()) {
                appointment.setTimeSlot(slots.get(slotIndex));
                System.out.println("Appointment modified successfully!");
            }
        } else {
            System.out.println("Appointment not found.");
        }
    }

    private static void modifyUserAppointment(User user) {
        List<Appointment> appointments = appointmentService.getUserAppointments(user);
        if (appointments.isEmpty()) {
            System.out.println("You have no appointments to modify.");
            return;
        }

        viewUserAppointments(user);
        System.out.print("Enter appointment ID to modify: ");
        String id = scanner.nextLine();

        Appointment appointment = appointmentService.findAppointment(id);
        if (appointment != null) {
            List<TimeSlot> slots = appointmentService.getAvailableSlots();
            viewAvailableSlots();
            System.out.print("Choose new slot number: ");
            int slotIndex = scanner.nextInt() - 1;
            scanner.nextLine();

            if (slotIndex >= 0 && slotIndex < slots.size()) {
                appointment.setTimeSlot(slots.get(slotIndex));
                System.out.println("Appointment modified successfully!");
            }
        } else {
            System.out.println("Appointment not found.");
        }
    }

    private static void cancelAppointment() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments to cancel.");
            return;
        }

        viewAllAppointments();
        System.out.print("Enter appointment ID to cancel: ");
        String id = scanner.nextLine();

        if (appointmentService.cancelAppointment(id, null)) {
            System.out.println("Appointment cancelled successfully!");
        } else {
            System.out.println("Failed to cancel appointment.");
        }
    }

    private static void cancelUserAppointment(User user) {
        List<Appointment> appointments = appointmentService.getUserAppointments(user);
        if (appointments.isEmpty()) {
            System.out.println("You have no appointments to cancel.");
            return;
        }

        viewUserAppointments(user);
        System.out.print("Enter appointment ID to cancel: ");
        String id = scanner.nextLine();

        if (appointmentService.cancelAppointment(id, user)) {
            System.out.println("Appointment cancelled successfully!");
        } else {
            System.out.println("Failed to cancel appointment.");
        }
    }
}