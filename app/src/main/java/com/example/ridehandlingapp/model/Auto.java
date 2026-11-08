package com.example.ridehandlingapp.model;

/**
 * Concrete subclass representing an Auto-Rickshaw.
 * Demonstrates:
 * 1. Inheritance: Extends Vehicle abstract class.
 * 2. Polymorphism & Method Overriding: Overrides calculateFare().
 * 3. Constructor Overloading: Multiple constructors for custom configurations.
 */
public class Auto extends Vehicle {

    private boolean meterInstalled;

    /**
     * Default constructor for Auto with standard city rates.
     */
    public Auto() {
        super("Auto", 35.0, 12.0, 3);
        this.meterInstalled = true;
    }

    /**
     * Overloaded constructor for Auto with custom rates.
     *
     * @param baseFare   Starting minimum fare.
     * @param perKmRate  Per-kilometer charging rate.
     */
    public Auto(double baseFare, double perKmRate) {
        super("Auto", baseFare, perKmRate, 3);
        this.meterInstalled = true;
    }

    /**
     * Method Overriding (Polymorphism):
     * Calculates the fare for an Auto ride: Base Fare + (Rate per km * distance).
     *
     * @param distance Distance in kilometers.
     * @return Computed total fare for auto.
     */
    @Override
    public double calculateFare(double distance) {
        return getBaseFare() + (getPerKmRate() * distance);
    }

    // Encapsulation getter & setter
    public boolean isMeterInstalled() {
        return meterInstalled;
    }

    public void setMeterInstalled(boolean meterInstalled) {
        this.meterInstalled = meterInstalled;
    }
}
