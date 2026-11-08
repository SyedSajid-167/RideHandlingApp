package com.example.ridehandlingapp.model;

import java.io.Serializable;

/**
 * Model class representing a Passenger/User in the ride-hailing app.
 * Demonstrates:
 * 1. Encapsulation: All fields are private with appropriate public accessors.
 * 2. Constructor Overloading: Multiple constructors for flexible creation.
 */
public class User implements Serializable {

    // Encapsulation: Private member variables
    private String userId;
    private String name;
    private String phoneNumber;
    private String email;

    /**
     * Default constructor creating a guest user.
     */
    public User() {
        this("U101", "Passenger", "+91 9876543210", "passenger@example.com");
    }

    /**
     * Overloaded constructor with name and phone number.
     *
     * @param name        Passenger's full name.
     * @param phoneNumber Contact mobile number.
     */
    public User(String name, String phoneNumber) {
        this("U-" + System.currentTimeMillis() % 1000, name, phoneNumber, "user@example.com");
    }

    /**
     * Fully parameterized constructor.
     *
     * @param userId      Unique identification string for the user.
     * @param name        User full name.
     * @param phoneNumber Contact phone number.
     * @param email       Email address.
     */
    public User(String userId, String name, String phoneNumber, String email) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Getters and Setters demonstrating Encapsulation

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
