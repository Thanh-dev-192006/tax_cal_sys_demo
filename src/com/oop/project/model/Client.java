package com.oop.project.model;

import java.io.Serializable;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    // Original fields (giu nguyen de tuong thich)
    private String id;              // Tax ID (SSN format: XXX-XX-XXXX)
    private String name;            // Full name
    private double income;          // Monthly income
    private String maritalStatus;   // SINGLE, MARRIED, DIVORCED, WIDOWED
    private String contactInfo;     // Deprecated - dung email thay the
    
    // Extended fields (them moi - match voi SQL)
    private int dependents;         // So nguoi phu thuoc
    private String email;           // Email address
    private String phoneNumber;     // Phone number
    private String city;            // City

    // Constructor cu (giu nguyen de backward compatible)
    public Client(String id, String name, double income, String maritalStatus, String contactInfo) {
        this.id = id;
        this.name = name;
        this.income = income;
        this.maritalStatus = maritalStatus;
        this.contactInfo = contactInfo;
        this.dependents = 0;
        this.email = contactInfo; // Default: contactInfo la email
        this.phoneNumber = "";
        this.city = "";
    }
    
    // Constructor moi (day du - cho seed data)
    public Client(String id, String name, double income, int dependents, 
                String maritalStatus, String email, String phoneNumber, String city) {
        this.id = id;
        this.name = name;
        this.income = income;
        this.dependents = dependents;
        this.maritalStatus = maritalStatus;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.contactInfo = email; // Backward compatible
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getIncome() { return income; }
    public void setIncome(double income) { this.income = income; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public int getDependents() { return dependents; }
    public void setDependents(int dependents) { this.dependents = dependents; }

    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = email;
        this.contactInfo = email; // Keep in sync
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", income=" + income +
                ", dependents=" + dependents +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
