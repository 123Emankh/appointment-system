/*package com.appointments.service;

import com.appointments.persistence.InMemoryRepository;
import com.appointments.domain.Administrator;

/**
 * خدمة المصادقة.
 * @author فريقك
 * @version 1.0
 */
/*public class AuthenticationService {
    private InMemoryRepository repo;
    private Administrator currentAdmin;

    public AuthenticationService(InMemoryRepository repo) {
        this.repo = repo;
    }

    public boolean login(String id, String password) {
        Administrator admin = repo.findAdminById(id);
        if (admin != null && admin.azuthenticate(password)) {
            currentAdmin = admin;
            return true;
        }
        System.out.println("");
        return false;
    }

    public void logout() {
        currentAdmin = null;
    }

    public boolean isLoggedIn() {
        return currentAdmin != null;
    }
}*/