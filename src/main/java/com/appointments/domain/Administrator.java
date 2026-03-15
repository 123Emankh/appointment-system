package com.appointments.domain;

public class Administrator extends User {
    private String adminId;
    private String role;

    public Administrator(String adminId, String password, String name) {
        super(adminId, password, name, "admin@system.com");
        this.adminId = adminId;
        this.role = "SUPER_ADMIN";
    }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}