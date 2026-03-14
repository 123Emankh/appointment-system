package com.appointments.domain;

/**
 * Represents an administrator in the system
 * Extends User with admin privileges
 * 
 * @author Team 3
 * @version 1.0
 */
public class Administrator extends User {
    private String adminId;
    private String role;

    /**
     * Constructor for Administrator
     * 
     * @param adminId unique admin identifier
     * @param password admin password
     * @param name admin full name
     */
    public Administrator(String adminId, String password, String name) {
        super(adminId, password, name, "admin@system.com");
        this.adminId = adminId;
        this.role = "SUPER_ADMIN";
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}