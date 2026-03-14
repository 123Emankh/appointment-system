package com.appointments.service;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private Map<String, String> users = new HashMap<>();

    public AuthService() {
        users.put("admin", "1234"); // مسؤول افتراضي
    }

    public boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public void logout(String username) {
        System.out.println(username + " logged out.");
    }
}