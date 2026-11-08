package com.example.ridehandlingapp.model;

/**
 * Interface demonstrating Abstraction and Contract-Driven Design in OOP.
 * Represents entities in the ride-hailing system that can be booked and cancelled.
 */
public interface Bookable {

    /**
     * Confirms the booking request.
     */
    void confirmBooking();

    /**
     * Cancels the active booking request.
     */
    void cancelBooking();

    /**
     * Retrieves the current booking lifecycle status.
     *
     * @return Status description string (e.g., Booked, Ongoing, Completed, Cancelled).
     */
    String getBookingStatus();
}
