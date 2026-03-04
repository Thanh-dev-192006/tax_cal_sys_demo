package com.oop.project.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Original fields
    private String username;
    private String password;        // In real app, this should be hashed
    private String role;            // "ADMIN" or "TAX_STAFF"
    
    // Extended fields (them moi - match voi SQL)
    private String staffId;         // NV001, NV002, ...
    private String fullName;        // Ho va ten day du
    private String email;           // Email address
    private String phoneNumber;     // Phone number

    // Constructor cu (giu nguyen de backward compatible)
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.staffId = "";
        this.fullName = username; // Default
        this.email = "";
        this.phoneNumber = "";
    }
    
    // Constructor moi (day du - cho seed data)
    public User(String staffId, String username, String password, String role,
                String fullName, String email, String phoneNumber) {
        this.staffId = staffId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return "User{" +
                "staffId='" + staffId + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}