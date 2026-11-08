package com.example.ridehandlingapp.model;

/**
 * Interface demonstrating Abstraction and Financial Contract Design in OOP.
 * Represents entities that require monetary calculation and payment processing.
 */
public interface Payable {

    /**
     * Calculates the final payable amount for the service.
     *
     * @return Payable fare in rupees.
     */
    double calculateTotalFare();

    /**
     * Checks whether the payment has been finalized.
     *
     * @return True if paid, false otherwise.
     */
    boolean isPaymentCompleted();

    /**
     * Marks the entity's payment status as settled.
     */
    void markAsPaid();
}
