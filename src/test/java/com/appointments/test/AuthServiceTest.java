
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
import com.appointments.persistence.InMemoryRepository;
import com.appointments.service.AuthService;
import com.appointments.domain.User;
import com.appointments.domain.Administrator;

public class AuthServiceTest {

    private AuthService authService;
    private InMemoryRepository repo;

    @BeforeEach
    void setup() {
        repo = new InMemoryRepository();

        User user = new User("user1", "1234", "Test User", "test@test.com");
        repo.saveUser(user);

        Administrator admin = new Administrator("admin1", "admin123", "Admin Name");        repo.saveAdministrator(admin);

        authService = new AuthService(repo);
    }


    @Test
    void testAdminLoginSuccess() {
        boolean result = authService.authenticateAdmin("admin1", "admin123");

        assertTrue(result);
        assertTrue(authService.isAdmin());
        assertNotNull(authService.getCurrentAdmin());
    }

    @Test
    void testAdminLoginFailWrongPassword() {
        boolean result = authService.authenticateAdmin("admin1", "wrong");

        assertFalse(result);
    }

    @Test
    void testAdminLoginFailNotFound() {
        boolean result = authService.authenticateAdmin("unknown", "admin123");

        assertFalse(result);
    }


    @Test
    void testUserLoginSuccess() {
        User user = authService.authenticateUser("user1", "1234");

        assertNotNull(user);
        assertEquals("user1", user.getUsername());
        assertNotNull(authService.getCurrentUser());
    }

    @Test
    void testUserLoginFailWrongPassword() {
        User user = authService.authenticateUser("user1", "wrong");

        assertNull(user);
    }

    @Test
    void testUserLoginFailNotFound() {
        User user = authService.authenticateUser("unknown", "1234");

        assertNull(user);
    }


    @Test
    void testLogout() {
        authService.authenticateUser("user1", "1234");

        authService.logout();

        assertNull(authService.getCurrentUser());
        assertNull(authService.getCurrentAdmin());
        assertFalse(authService.isAdmin());
    }
}