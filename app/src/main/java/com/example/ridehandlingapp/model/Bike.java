package com.example.ridehandlingapp.model;

/**
 * Concrete subclass representing a Two-Wheeler Bike.
 * Demonstrates:
 * 1. Inheritance: Extends Vehicle abstract class.
 * 2. Polymorphism & Method Overriding: Overrides calculateFare().
 * 3. Constructor Overloading: Multiple constructors for flexible instantiation.
 */
public class Bike extends Vehicle {

    private boolean helmetProvided;

    /**
     * Default constructor for Bike with preset rates.
     */
    public Bike() {
        super("Bike", 20.0, 8.0, 1);
        this.helmetProvided = true;
    }

    /**
     * Overloaded constructor allowing custom base fare and per km rate.
     *
     * @param baseFare   Starting minimum fare for the bike ride.
     * @param perKmRate  Per-kilometer charging rate.
     */
    public Bike(double baseFare, double perKmRate) {
        super("Bike", baseFare, perKmRate, 1);
        this.helmetProvided = true;
    }

    /**
     * Method Overriding (Polymorphism):
     * Calculates the fare for a Bike ride: Base Fare + (Rate per km * distance).
     *
     * @param distance Distance in kilometers.
     * @return Computed total fare for bike.
     */
    @Override
    public double calculateFare(double distance) {
        return getBaseFare() + (getPerKmRate() * distance);
    }

    // Encapsulation getter & setter
    public boolean isHelmetProvided() {
        return helmetProvided;
    }

    public void setHelmetProvided(boolean helmetProvided) {
        this.helmetProvided = helmetProvided;
    }
}
