package com.appointments;
import java.time.format.DateTimeFormatter;
import com.appointments.domain.*;
import com.appointments.persistence.InMemoryRepository;
import com.appointments.service.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
/**
 * Entry point of the Appointment Scheduling System.
 * <p>
 * This class handles system initialization, user interaction via console,
 *authentication, and navigation between admin and user menus.
 * It connects all services including authentication, appointments,
 * and notifications.
 * </p>
 *
 * @author Eman
 * @version 1.0
 */
public class Main {
    private static AuthService authService;
    private static AppointmentService appointmentService;
    private static InMemoryRepository repository;
    private static Scanner scanner;
    private static NotificationService notificationService;

    /**
     * Main method that starts the application.
     *
     * @param args command line arguments
     */
    
    public static void main(String[] args) {
        initializeSystem();
        runApplication();
    }
    /**
     * Main method that starts the application.
     *
     * @param args command line arguments
     */
    private static void initializeSystem() {
        scanner = new Scanner(System.in);

        boolean isTest = "true".equals(System.getProperty("test.mode"));
        
        EmailService emailService;
        if (isTest) {
            emailService = new EmailService("dummy", "dummy") {
                @Override
                public void sendEmail(String to, String subject, String body) {
                }
            };
        } else {
            String gmailUser = "ekh9951@gmail.com";          
            String gmailAppPass = "crqw cxbe cnas lnpz";
            emailService = new EmailService(gmailUser, gmailAppPass);
        }
        
        notificationService = new NotificationService(emailService);
        repository = new InMemoryRepository();
        authService = new AuthService(repository);
        appointmentService = new AppointmentService(repository, notificationService);

        appointmentService.addBookingRule(new DurationRuleStrategy(120));
        appointmentService.addBookingRule(new ParticipantLimitStrategy());
        appointmentService.addBookingRule(new TypeSpecificRuleStrategy());
    }

    /**
     * Runs the main application loop and displays the main menu.
     */

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
    /**
     * Handles administrator login.
     */
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

    /**
     * Handles user login.
     */
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
    /**
     * Displays the administrator menu and handles admin operations.
     */
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
    /**
     * Displays the user menu and handles user operations.
     *
     * @param user the logged-in user
     */
    private static void userMenu(User user) {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. View available slots");
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

    /**
     * Displays all available appointment slots.
     */
    
    private static void viewAvailableSlots() {
        List<TimeSlot> slots = appointmentService.getAvailableSlots();
        if (slots.isEmpty()) {
            System.out.println("No available slots.");
        } else {
            System.out.println("\nAvailable appointments:");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (int i = 0; i < slots.size(); i++) {
                String formatted = slots.get(i).getStart().format(formatter);
                System.out.println((i + 1) + ". " + formatted);
            }
        }
    }


    /**
     * Books an appointment for a given user.
     *
     * @param user the user booking the appointment
     */
    private static void bookAppointment(User user) {
        List<TimeSlot> slots = appointmentService.getAvailableSlots();
        if (slots.isEmpty()) {
            System.out.println("No available slots.");
            return;
        }

        viewAvailableSlots();
        System.out.print("Choose slot number (or 0 to cancel): ");
        int slotIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (slotIndex == -1) {
            System.out.println("Booking cancelled.");
            return;
        }

        if (slotIndex >= 0 && slotIndex < slots.size()) {
            System.out.println("Select appointment type (or 0 to cancel):");
            System.out.println("1. Individual");
            System.out.println("2. Group");
            System.out.println("3. Virtual");
            System.out.println("4. In-person");
            System.out.print("Choose type: ");
            int typeChoice = scanner.nextInt();
            scanner.nextLine();

            if (typeChoice == 0) {
                System.out.println("Booking cancelled.");
                return;
            }

            AppointmentType type;
            switch (typeChoice) {
                case 1: type = AppointmentType.INDIVIDUAL; break;
                case 2: type = AppointmentType.GROUP; break;
                case 3: type = AppointmentType.VIRTUAL; break;
                case 4: type = AppointmentType.IN_PERSON; break;
                default: type = AppointmentType.INDIVIDUAL;
            }

            TimeSlot selectedSlot = slots.get(slotIndex);
            String id = UUID.randomUUID().toString();
            Appointment appointment = new Appointment(id, user, selectedSlot, type, "Confirmed");

            try {
                appointmentService.bookAppointment(appointment);
                System.out.println(" Appointment booked successfully!");
            } catch (IllegalArgumentException e) {
                System.out.println("❌ " + e.getMessage());
            }
        } else {
            System.out.println("Invalid slot number.");
        }
    }

    /**
     * Displays appointments of a specific user.
     *
     * @param user the user whose appointments are displayed
     */
    
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

    /**
     * Displays all appointments in the system.
     */
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
        System.out.println("Enter new slot details (format: yyyy-MM-dd HH:mm): ");
        scanner.nextLine(); 
        String dateTimeStr = scanner.nextLine();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime start = LocalDateTime.parse(dateTimeStr, formatter);
            LocalDateTime end = start.plusHours(1);
            TimeSlot newSlot = new TimeSlot(start, end);
            
            if (repository.isSlotOverlapping(newSlot)) {
                System.out.println(" This time slot overlaps with an existing available slot. Please choose another time.");
                return;
            }
            
            appointmentService.addAvailableSlot(newSlot);
            System.out.println(" Slot added successfully!");
        } catch (Exception e) {
            System.out.println(" Invalid format. Please use yyyy-MM-dd HH:mm (e.g., 2026-04-12 10:00)");
        }
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
            if (slots.isEmpty()) {
                System.out.println("No available slots to change to.");
                return;
            }
            viewAvailableSlots();
            System.out.print("Choose new slot number: ");
            int slotIndex = scanner.nextInt() - 1;
            scanner.nextLine();

            if (slotIndex >= 0 && slotIndex < slots.size()) {
                TimeSlot newSlot = slots.get(slotIndex);
                try {
                    appointmentService.modifyAppointment(id, newSlot);
                    System.out.println(" Appointment modified successfully!");
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ " + e.getMessage());
                }
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
            if (slots.isEmpty()) {
                System.out.println("No available slots to change to.");
                return;
            }
            viewAvailableSlots();
            System.out.print("Choose new slot number: ");
            int slotIndex = scanner.nextInt() - 1;
            scanner.nextLine();

            if (slotIndex >= 0 && slotIndex < slots.size()) {
                TimeSlot newSlot = slots.get(slotIndex);
                try {
                    appointmentService.modifyAppointment(id, newSlot);
                    System.out.println(" Appointment modified successfully!");
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ " + e.getMessage());
                }
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

        try {
            appointmentService.cancelAppointment(id, null); // admin cancels
            System.out.println(" Appointment cancelled successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
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

        try {
            appointmentService.cancelAppointment(id, user);
            System.out.println("Appointment cancelled successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private Main() {
    }
    

/**
 * For testing purposes - allows injecting a custom scanner
 */
public static void setScanner(Scanner testScanner) {
    scanner = testScanner;
}

/**
 * For testing purposes - resets the scanner to System.in
 */
public static void resetScanner() {
    scanner = new Scanner(System.in);
}

/**
 * For testing purposes - allows accessing the authService
 */
public static AuthService getAuthService() {
    return authService;
}

/**
 * For testing purposes - allows accessing the appointmentService
 */
public static AppointmentService getAppointmentService() {
    return appointmentService;
}
}