package com.example.ridehandlingapp.model;

import java.io.Serializable;

/**
 * Abstract class representing a general Vehicle in the ride-hailing system.
 * Demonstrates OOP Concepts:
 * 1. Abstraction: Defines abstract method calculateFare() without implementation.
 * 2. Encapsulation: All attributes are private, exposed via getters and setters.
 * 3. Polymorphism: Subclasses (Bike, Auto, Cab) provide distinct implementations.
 * 4. Constructor Overloading: Multiple constructors provided.
 */
public abstract class Vehicle implements Serializable {

    // Encapsulation: Private member variables
    private String name;
    private double baseFare;
    private double perKmRate;
    private int capacity;

    /**
     * Default constructor for Vehicle.
     */
    public Vehicle() {
        this.name = "Standard Vehicle";
        this.baseFare = 30.0;
        this.perKmRate = 10.0;
        this.capacity = 4;
    }

    /**
     * Overloaded constructor initializing vehicle specifications.
     *
     * @param name       Display name of the vehicle.
     * @param baseFare   Starting minimum fare.
     * @param perKmRate  Rate charged per kilometer.
     * @param capacity   Passenger seating capacity.
     */
    public Vehicle(String name, double baseFare, double perKmRate, int capacity) {
        this.name = name;
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
        this.capacity = capacity;
    }

    /**
     * Abstract method demonstrating Polymorphism.
     * Each vehicle subclass must provide its own formula for fare computation.
     *
     * @param distance Distance traversed in kilometers.
     * @return Computed fare amount.
     */
    public abstract double calculateFare(double distance);

    // Getters and Setters demonstrating Encapsulation

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public double getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(double perKmRate) {
        this.perKmRate = perKmRate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
