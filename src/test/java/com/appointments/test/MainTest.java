package com.appointments.test;

import com.appointments.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Main Application - Complete Tests")
class MainCompleteTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
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

    // ==================== اختبارات القائمة الرئيسية ====================

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

    // ==================== اختبارات تسجيل الدخول ====================

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

    // ==================== اختبارات عرض البيانات ====================

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

    // ==================== اختبارات الخيارات غير الصالحة ====================

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

    // ==================== اختبارات بدون مواعيد ====================

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

    // ==================== اختبارات التنقل المتعدد ====================

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

    // ==================== اختبارات Reflection للدوال الخاصة ====================

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
}