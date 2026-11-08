package com.example.ridehandlingapp.logic;

import com.example.ridehandlingapp.model.Vehicle;

/**
 * Service class for computing ride fares.
 * Demonstrates:
 * 1. Method Overloading: Multiple calculateFare() methods with varying parameter signatures.
 * 2. Polymorphism: Delegates calculation to Vehicle.calculateFare(distance).
 * 3. Static Utility Design: Pure computational functions.
 */
public class FareCalculator {

    /**
     * Overloaded method 1:
     * Calculates base fare for a given polymorphic vehicle and distance.
     *
     * @param vehicle  Selected vehicle instance (Bike, Auto, or Cab).
     * @param distance Distance in kilometers.
     * @return Computed fare rounded to 2 decimal places.
     */
    public static double calculateFare(Vehicle vehicle, double distance) {
        if (vehicle == null || distance <= 0) {
            return 0.0;
        }
        // Polymorphic call: Subclass implementation invoked at runtime
        double rawFare = vehicle.calculateFare(distance);
        return Math.round(rawFare * 100.0) / 100.0;
    }

    /**
     * Overloaded method 2:
     * Calculates fare considering demand surge pricing multiplier.
     *
     * @param vehicle         Selected vehicle instance.
     * @param distance        Distance in kilometers.
     * @param surgeMultiplier Surge multiplier (e.g., 1.2 for 20% peak demand).
     * @return Final fare factoring in surge.
     */
    public static double calculateFare(Vehicle vehicle, double distance, double surgeMultiplier) {
        double standardFare = calculateFare(vehicle, distance);
        double multiplier = Math.max(1.0, surgeMultiplier);
        return Math.round(standardFare * multiplier * 100.0) / 100.0;
    }

    /**
     * Overloaded method 3:
     * Calculates fare based on explicit numeric base fare, per-km rate, and distance.
     *
     * @param baseFare   Starting minimum charge.
     * @param perKmRate  Per-kilometer charge.
     * @param distance   Total distance in kilometers.
     * @return Computed fare.
     */
    public static double calculateFare(double baseFare, double perKmRate, double distance) {
        if (distance <= 0) {
            return baseFare;
        }
        double total = baseFare + (perKmRate * distance);
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Overloaded method 4:
     * Calculates fare from primitive rates with surge multiplier.
     *
     * @param baseFare        Base charge.
     * @param perKmRate       Per-km rate.
     * @param distance        Distance in km.
     * @param surgeMultiplier Surge rate factor.
     * @return Surge-adjusted fare.
     */
    public static double calculateFare(double baseFare, double perKmRate, double distance, double surgeMultiplier) {
        double baseCalculated = calculateFare(baseFare, perKmRate, distance);
        double multiplier = Math.max(1.0, surgeMultiplier);
        return Math.round(baseCalculated * multiplier * 100.0) / 100.0;
    }
}
