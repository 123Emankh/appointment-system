package com.appointments.service;

import com.appointments.domain.Administrator;
import com.appointments.domain.User;
import com.appointments.persistence.InMemoryRepository;

public class AuthenticationService {
    private InMemoryRepository repository;
    private User currentUser;
    private Administrator currentAdmin;

    public AuthenticationService(InMemoryRepository repository) {
        this.repository = repository;
        this.currentUser = null;
        this.currentAdmin = null;
    }

    public boolean authenticateAdmin(String adminId, String password) {
        Administrator admin = repository.findAdministrator(adminId);
        if (admin != null && admin.getPassword().equals(password)) {
            currentAdmin = admin;
            currentUser = null;
            return true;
        }
        return false;
    }

    public User authenticateUser(String username, String password) {
        User user = repository.findUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            currentAdmin = null;
            return user;
        }
        return null;
    }

    public void logout() {
        currentUser = null;
        currentAdmin = null;
    }

    public boolean isAdmin() {
        return currentAdmin != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Administrator getCurrentAdmin() {
        return currentAdmin;
    }
}