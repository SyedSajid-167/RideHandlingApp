package com.example.ridehandlingapp.model;

import java.io.Serializable;

/**
 * Model class representing a Driver in the ride-hailing app.
 * Demonstrates:
 * 1. Encapsulation: Private attributes with public getters and setters.
 * 2. Constructor Overloading: Multiple constructors for diverse driver profiles.
 */
public class Driver implements Serializable {

    // Encapsulation: Private member variables
    private String driverId;
    private String name;
    private String phoneNumber;
    private String vehicleModel;
    private String vehicleNumber;
    private double rating;

    /**
     * Default constructor initializing a verified top-rated driver.
     */
    public Driver() {
        this("D-101", "Rajesh Kumar", "+91 9823456789", "White Swift Dzire", "MH 02 AB 1234", 4.8);
    }

    /**
     * Overloaded constructor with essential details.
     *
     * @param name          Driver full name.
     * @param phoneNumber   Driver contact phone.
     * @param vehicleModel  Model and color of vehicle.
     * @param vehicleNumber License plate number.
     */
    public Driver(String name, String phoneNumber, String vehicleModel, String vehicleNumber) {
        this("D-" + (int)(Math.random() * 900 + 100), name, phoneNumber, vehicleModel, vehicleNumber, 4.7);
    }

    /**
     * Fully parameterized constructor.
     *
     * @param driverId      Unique driver identifier.
     * @param name          Driver full name.
     * @param phoneNumber   Driver contact phone number.
     * @param vehicleModel  Vehicle model details.
     * @param vehicleNumber License plate registration number.
     * @param rating        Driver's aggregate customer rating (1.0 to 5.0).
     */
    public Driver(String driverId, String name, String phoneNumber, String vehicleModel,
                  String vehicleNumber, double rating) {
        this.driverId = driverId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.vehicleModel = vehicleModel;
        this.vehicleNumber = vehicleNumber;
        this.rating = rating;
    }

    // Getters and Setters demonstrating Encapsulation

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
