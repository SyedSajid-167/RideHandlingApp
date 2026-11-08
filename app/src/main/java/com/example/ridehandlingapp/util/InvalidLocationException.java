package com.example.ridehandlingapp.util;

/**
 * Custom Exception class to demonstrate OOP Exception Handling.
 * Thrown when user inputs invalid pickup or drop locations
 * (e.g., empty string, identical locations, or too short).
 */
public class InvalidLocationException extends Exception {

    /**
     * Default constructor for InvalidLocationException.
     */
    public InvalidLocationException() {
        super("Invalid location provided. Please verify pickup and destination points.");
    }

    /**
     * Overloaded constructor accepting a custom error message.
     *
     * @param message Detailed description of the validation failure.
     */
    public InvalidLocationException(String message) {
        super(message);
    }

    /**
     * Overloaded constructor accepting a custom error message and root cause.
     *
     * @param message Detailed description of the validation failure.
     * @param cause   Root throwable cause.
     */
    public InvalidLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
