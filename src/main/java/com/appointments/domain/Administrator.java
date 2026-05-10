/**
 * Contains the domain entities and core models of the appointment system.
 */
package com.appointments.domain;

/**
 * Represents an administrator in the system.
 * <p>
 * The Administrator class extends {@link User} and adds
 * administrative-specific attributes such as adminId and role.
 * </p>
 *
 * @author Eman
 * @version 1.0
 */
public class Administrator extends User {

    private String adminId;
    private String role;

    /**
     * Constructs a new Administrator instance.
     *
     * @param adminId the unique identifier of the administrator
     * @param password the password used for authentication
     * @param name the name of the administrator
     */
    public Administrator(String adminId, String password, String name) {
        super(adminId, password, name, "admin@system.com");
        this.adminId = adminId;
        this.role = "SUPER_ADMIN";
    }

    /**
     * Returns the administrator ID.
     *
     * @return the adminId
     */
    public String getAdminId() {
        return adminId;
    }

    /**
     * Sets the administrator ID.
     *
     * @param adminId the new adminId
     */
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    /**
     * Returns the role of the administrator.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the administrator.
     *
     * @param role the new role
     */
    public void setRole(String role) {
        this.role = role;
    }
}