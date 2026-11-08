package com.example.ridehandlingapp.model;

/**
 * Concrete subclass representing a Four-Wheeler Cab (Sedan / Hatchback).
 * Demonstrates:
 * 1. Inheritance: Extends Vehicle abstract class.
 * 2. Polymorphism & Method Overriding: Overrides calculateFare() with AC premium logic.
 * 3. Constructor Overloading: Overloaded constructors for custom configurations.
 */
public class Cab extends Vehicle {

    private boolean airConditioned;

    /**
     * Default constructor for Cab with standard rates.
     */
    public Cab() {
        super("Cab", 60.0, 18.0, 4);
        this.airConditioned = true;
    }

    /**
     * Overloaded constructor for Cab with custom rates.
     *
     * @param baseFare   Starting minimum fare.
     * @param perKmRate  Per-kilometer charging rate.
     */
    public Cab(double baseFare, double perKmRate) {
        super("Cab", baseFare, perKmRate, 4);
        this.airConditioned = true;
    }

    /**
     * Method Overriding (Polymorphism):
     * Calculates the fare for a Cab ride: Base Fare + (Rate per km * distance).
     *
     * @param distance Distance in kilometers.
     * @return Computed total fare for cab.
     */
    @Override
    public double calculateFare(double distance) {
        return getBaseFare() + (getPerKmRate() * distance);
    }

    // Encapsulation getter & setter
    public boolean isAirConditioned() {
        return airConditioned;
    }

    public void setAirConditioned(boolean airConditioned) {
        this.airConditioned = airConditioned;
    }
}
