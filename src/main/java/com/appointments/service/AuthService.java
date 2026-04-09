package com.appointments.service;

import com.appointments.domain.Administrator;
import com.appointments.domain.User;
import com.appointments.persistence.InMemoryRepository;

/**
 * Service responsible for authentication of users and administrators.
 *
 * @author Eman
 * @version 1.0
 */
public class AuthService {

    private InMemoryRepository repository;
    private User currentUser;
    private Administrator currentAdmin;

    /**
     * Constructs the AuthService.
     *
     * @param repository the data repository
     */
    public AuthService(InMemoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Authenticates an administrator.
     *
     * @param adminId admin ID
     * @param password password
     * @return true if authentication is successful
     */
    public boolean authenticateAdmin(String adminId, String password) {
        Administrator admin = repository.findAdministrator(adminId);
        if (admin != null && admin.getPassword().equals(password)) {
            currentAdmin = admin;
            currentUser = null;
            return true;
        }
        return false;
    }

    /**
     * Authenticates a user.
     *
     * @param username username
     * @param password password
     * @return the authenticated user or null if failed
     */
    public User authenticateUser(String username, String password) {
        User user = repository.findUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            currentAdmin = null;
            return user;
        }
        return null;
    }

    /**
     * Logs out the current user/admin.
     */
    public void logout() {
        currentUser = null;
        currentAdmin = null;
    }

    
    /**
     * Checks if the currently logged-in user is an administrator.
     *
     * @return true if an administrator is logged in, false otherwise
     */
    public boolean isAdmin() {
        return currentAdmin != null;
    }

    /**
     * Returns the currently logged-in regular user, or null if none.
     *
     * @return the current User, or null
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Returns the currently logged-in administrator, or null if none.
     *
     * @return the current Administrator, or null
     */
    public Administrator getCurrentAdmin() {
        return currentAdmin;
    }
}