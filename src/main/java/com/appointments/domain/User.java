package com.appointments.domain;

/**
 * Represents a regular user in the system
 * 
 * @author Team 3
 * @version 1.0
 */
public class User {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;

    /**
     * Constructor for User
     * 
     * @param username username for login
     * @param password password for login
     * @param name full name
     * @param email email address
     */
    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }

    // أضف هذه الطريقة
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}