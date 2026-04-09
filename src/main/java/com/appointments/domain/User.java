package com.appointments.domain;

/**
 * Represents a user in the appointment system.
 * <p>
 * A user contains basic authentication information along with personal details
 * such as name, email, and phone number.
 * </p>
 *
 * @author Eman
 * @version 1.0
 */
public class User {

    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;

    /**
     * Constructs a new User instance.
     *
     * @param username the unique username of the user
     * @param password the password for authentication
     * @param name the full name of the user
     * @param email the email address of the user
     */
    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * Sets the username of the user.
     *
     * @param username the new username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Returns the password of the user.
     *
     * @return the password
     */
    public String getPassword() { return password; }

    /**
     * Sets the password of the user.
     *
     * @param password the new password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Returns the name of the user.
     *
     * @return the full name
     */
    public String getName() { return name; }

    /**
     * Sets the name of the user.
     *
     * @param name the new full name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the email of the user.
     *
     * @return the email address
     */
    public String getEmail() { return email; }

    /**
     * Sets the email of the user.
     *
     * @param email the new email address
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Returns the phone number of the user.
     *
     * @return the phone number
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber the new phone number
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * Compares this user with another object for equality based on username.
     *
     * @param obj the object to compare with
     * @return true if usernames are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username != null ? username.equals(user.username) : user.username == null;
    }

    /**
     * Returns the hash code of the user based on username.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}