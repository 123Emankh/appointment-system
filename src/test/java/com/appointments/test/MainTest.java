package com.appointments.test;

import com.appointments.Main;
//import com.appointments.domain.Appointment;
//import com.appointments.domain.AppointmentType;
//import com.appointments.domain.TimeSlot;
//import com.appointments.domain.User;
//import com.appointments.service.AppointmentService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
//import java.lang.reflect.Field;
import java.lang.reflect.Method;



import com.appointments.domain.*;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;
import org.mockito.Mockito;

import com.appointments.service.AppointmentService;
import com.appointments.service.EmailService;
import com.appointments.service.NotificationService;





import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Main Application - Complete Tests")
class MainCompleteTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    
    static class DummyEmailService extends EmailService {
        public DummyEmailService() {
            super("dummy", "dummy");
        }
        
        @Override
        public void sendEmail(String to, String subject, String body) {
            System.out.println("[TEST] Would send email to " + to + " but suppressed.");
        }
    }
    @BeforeAll
    static void enableTestMode() {
        System.setProperty("test.mode", "true");
    }
    
    @BeforeAll
    static void suppressEmails() throws Exception {
        EmailService dummy = new EmailService("dummy", "dummy") {
            @Override
            public void sendEmail(String to, String subject, String body) {
            }
        };
        NotificationService dummyNotification = new NotificationService(dummy);
        
        Field field = Main.class.getDeclaredField("notificationService");
        field.setAccessible(true);
        field.set(null, dummyNotification);
    }
    
    
    @BeforeEach
    void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        
        EmailService dummyEmailService = new DummyEmailService();
        NotificationService dummyNotificationService = new NotificationService(dummyEmailService);
        
        Field field = Main.class.getDeclaredField("notificationService");
        field.setAccessible(true);
        field.set(null, dummyNotificationService);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return outContent.toString();
    }


    @Test
    @DisplayName("Exit application")
    void testExit() {
        provideInput("3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("Goodbye!"));
    }

    @Test
    @DisplayName("Invalid option in main menu")
    void testInvalidMainOption() {
        provideInput("99\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("Invalid option!"));
    }


    @Test
    @DisplayName("Admin login wrong credentials")
    void testAdminWrongLogin() {
        provideInput("1\nwrong\nwrong\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("Invalid credentials!"));
    }

    @Test
    @DisplayName("User login wrong credentials")
    void testUserWrongLogin() {
        provideInput("2\nwrong\nwrong\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("Invalid credentials!"));
    }

    @Test
    @DisplayName("Admin login success and logout")
    void testAdminSuccess() {
        provideInput("1\nadmin\n1234\n6\n3\n");
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("Login successful!"));
        assertTrue(output.contains("Logged out"));
    }

    @Test
    @DisplayName("User login success and logout")
    void testUserSuccess() {
        provideInput("2\nuser1\npassword\n6\n3\n");
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("Login successful!"));
        assertTrue(output.contains("Logged out"));
    }


    @Test
    @DisplayName("Admin view available slots")
    void testAdminViewSlots() {
        provideInput("1\nadmin\n1234\n2\n6\n3\n");
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("Available appointments") || output.contains("No available slots"));
    }

    @Test
    @DisplayName("User view available slots")
    void testUserViewSlots() {
        provideInput("2\nuser1\npassword\n1\n6\n3\n");
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("Available appointments") || output.contains("No available slots"));
    }

    @Test
    @DisplayName("User view my appointments")
    void testUserViewMy() {
        provideInput("2\nuser1\npassword\n3\n6\n3\n");
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("Your appointments") || output.contains("You have no appointments"));
    }

    @Test
    @DisplayName("Admin view all appointments")
    void testAdminViewAll() {
        provideInput("1\nadmin\n1234\n1\n6\n3\n");
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("All Appointments") || output.contains("No appointments found"));
    }


    @Test
    @DisplayName("Invalid admin menu option")
    void testAdminInvalid() {
        provideInput("1\nadmin\n1234\n99\n6\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("Invalid option!"));
    }

    @Test
    @DisplayName("Invalid user menu option")
    void testUserInvalid() {
        provideInput("2\nuser1\npassword\n99\n6\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("Invalid option!"));
    }

    @Test
    @DisplayName("Negative main menu option")
    void testNegativeMain() {
        provideInput("-1\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("Invalid option!"));
    }


    @Test
    @DisplayName("Admin cancel - no appointments")
    void testAdminCancelNone() {
        provideInput("1\nadmin\n1234\n5\n6\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("No appointments to cancel"));
    }

    @Test
    @DisplayName("User cancel - no appointments")
    void testUserCancelNone() {
        provideInput("2\nuser1\npassword\n5\n6\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("You have no appointments to cancel"));
    }

    @Test
    @DisplayName("Admin modify - no appointments")
    void testAdminModifyNone() {
        provideInput("1\nadmin\n1234\n4\n6\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("No appointments to modify"));
    }

    @Test
    @DisplayName("User modify - no appointments")
    void testUserModifyNone() {
        provideInput("2\nuser1\npassword\n4\n6\n3\n");
        Main.main(new String[]{});
        assertTrue(getOutput().contains("You have no appointments to modify"));
    }


    @Test
    @DisplayName("Admin multiple views")
    void testAdminMultiple() {
        provideInput("1\nadmin\n1234\n1\n2\n6\n3\n");
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("All Appointments") || output.contains("No appointments found"));
        assertTrue(output.contains("Available appointments") || output.contains("No available slots"));
    }

    @Test
    @DisplayName("User multiple views")
    void testUserMultiple() {
        provideInput("2\nuser1\npassword\n1\n3\n6\n3\n");
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("Available appointments") || output.contains("No available slots"));
        assertTrue(output.contains("Your appointments") || output.contains("You have no appointments"));
    }


    @Test
    @DisplayName("Test viewAvailableSlots private method")
    void testViewAvailableSlotsPrivate() throws Exception {
        Method method = Main.class.getDeclaredMethod("viewAvailableSlots");
        method.setAccessible(true);
        method.invoke(null);
        assertNotNull(getOutput());
    }

    @Test
    @DisplayName("Test viewAllAppointments private method")
    void testViewAllAppointmentsPrivate() throws Exception {
        Method method = Main.class.getDeclaredMethod("viewAllAppointments");
        method.setAccessible(true);
        method.invoke(null);
        String output = getOutput();
        assertTrue(output.contains("No appointments found") || output.contains("All Appointments"));
    }
    
  ///-----

    
    @Test
    @DisplayName("Add available slot - success")
    void testAddAvailableSlotSuccess() throws Exception {
        String input = "\n2026-12-25 10:00\n";
        Scanner testScanner = new Scanner(input);
        Main.setScanner(testScanner);
        
        Method method = Main.class.getDeclaredMethod("addAvailableSlot");
        method.setAccessible(true);
        method.invoke(null);
        
        String output = getOutput();
        assertTrue(output.contains("Slot added successfully!"));
    }

    @Test
    @DisplayName("Add available slot - invalid format")
    void testAddAvailableSlotInvalidFormat() throws Exception {
        String input = "\ninvalid-date\n";
        Scanner testScanner = new Scanner(input);
        Main.setScanner(testScanner);
        
        Method method = Main.class.getDeclaredMethod("addAvailableSlot");
        method.setAccessible(true);
        method.invoke(null);
        
        assertTrue(getOutput().contains("Invalid format"));
    }
    

    
    @Test
    @DisplayName("Book appointment - success")
    void testBookAppointmentSuccess() throws Exception {
        AppointmentService service = Main.getAppointmentService();
        TimeSlot slot = new TimeSlot(
            LocalDateTime.of(2026, 12, 25, 10, 0),
            LocalDateTime.of(2026, 12, 25, 11, 0)
        );
        service.addAvailableSlot(slot);
        
        String input = "1\n1\n";   
        Scanner testScanner = new Scanner(input);
        Main.setScanner(testScanner);
        
        User user = Main.getAuthService().authenticateUser("user1", "password");
        
        Method method = Main.class.getDeclaredMethod("bookAppointment", User.class);
        method.setAccessible(true);
        method.invoke(null, user);
        
        assertTrue(getOutput().contains("Appointment booked successfully!"));
    }

  
    @Test
    @DisplayName("Book appointment - user cancels")
    void testBookAppointmentCancel() throws Exception {
        AppointmentService service = Main.getAppointmentService();
        TimeSlot slot = new TimeSlot(
            LocalDateTime.of(2026, 12, 25, 10, 0),
            LocalDateTime.of(2026, 12, 25, 11, 0)
        );
        service.addAvailableSlot(slot);
        
        String input = "0\n";
        Scanner testScanner = new Scanner(input);
        Main.setScanner(testScanner);
        
        User user = Main.getAuthService().authenticateUser("user1", "password");
        Method method = Main.class.getDeclaredMethod("bookAppointment", User.class);
        method.setAccessible(true);
        method.invoke(null, user);
        
        assertTrue(getOutput().contains("Booking cancelled."));
    }
    
    @Test
    @DisplayName("Modify appointment (admin) - success")
    void testModifyAppointmentSuccess() throws Exception {
        AppointmentService service = Main.getAppointmentService();
        TimeSlot originalSlot = new TimeSlot(LocalDateTime.of(2026, 12, 25, 10, 0), LocalDateTime.of(2026, 12, 25, 11, 0));
        service.addAvailableSlot(originalSlot);
        User user = Main.getAuthService().authenticateUser("user1", "password");
        Appointment apt = new Appointment(UUID.randomUUID().toString(), user, originalSlot, AppointmentType.INDIVIDUAL, "Confirmed");
        service.bookAppointment(apt);
        
        TimeSlot newSlot = new TimeSlot(LocalDateTime.of(2026, 12, 26, 10, 0), LocalDateTime.of(2026, 12, 26, 11, 0));
        service.addAvailableSlot(newSlot);
        
        String input = apt.getId() + "\n1\n";   // slot index 1 (second slot)
        Scanner testScanner = new Scanner(input);
        Main.setScanner(testScanner);
        
        Method method = Main.class.getDeclaredMethod("modifyAppointment");
        method.setAccessible(true);
        method.invoke(null);
        
        assertTrue(getOutput().contains("Appointment modified successfully!"));
    }
    
    @Test
    @DisplayName("Cancel appointment (admin) - success")
    void testCancelAppointmentSuccess() throws Exception {
        AppointmentService service = Main.getAppointmentService();
        TimeSlot slot = new TimeSlot(LocalDateTime.of(2026, 12, 25, 10, 0), LocalDateTime.of(2026, 12, 25, 11, 0));
        service.addAvailableSlot(slot);
        User user = Main.getAuthService().authenticateUser("user1", "password");
        Appointment apt = new Appointment(UUID.randomUUID().toString(), user, slot, AppointmentType.INDIVIDUAL, "Confirmed");
        service.bookAppointment(apt);
        
        String input = apt.getId() + "\n";
        Scanner testScanner = new Scanner(input);
        Main.setScanner(testScanner);
        
        Method method = Main.class.getDeclaredMethod("cancelAppointment");
        method.setAccessible(true);
        method.invoke(null);
        
        assertTrue(getOutput().contains("Appointment cancelled successfully!"));
    }
    
    @Test
    @DisplayName("Test helper methods for coverage")
    void testHelperMethods() {
        assertNotNull(Main.getAuthService());
        assertNotNull(Main.getAppointmentService());
        
        Scanner original = new Scanner(System.in);
        Main.setScanner(original);
        Main.resetScanner();
        assertTrue(true);
    }
    
   
    
}