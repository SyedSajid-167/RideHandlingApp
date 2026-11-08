package com.example.ridehandlingapp.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Core model class representing a booked Ride.
 * Demonstrates:
 * 1. Interface Implementation: Implements Bookable and Payable (Multiple Interface Realization).
 * 2. Encapsulation: Private attributes with getters and setters.
 * 3. Constructor Overloading: Overloaded constructors for convenient initialization.
 * 4. Aggregation / Composition: Holds User, Driver, and polymorphic Vehicle references.
 */
public class Ride implements Bookable, Payable, Serializable {

    // Encapsulation: Private member variables
    private String rideId;
    private String pickupLocation;
    private String dropLocation;
    private Vehicle vehicle;
    private double distanceKm;
    private double fare;
    private Driver driver;
    private User user;
    private String status; // "Booked", "Driver Arriving", "Ongoing", "Completed", "Cancelled"
    private float rating; // 0.0 to 5.0
    private String feedback;
    private String bookingTime;
    private boolean isPaid;

    /**
     * Default constructor for serialization and reflection.
     */
    public Ride() {
        this.rideId = "RIDE-" + System.currentTimeMillis() % 10000;
        this.status = "Booked";
        this.bookingTime = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(new Date());
        this.isPaid = false;
        this.rating = 0.0f;
        this.feedback = "";
    }

    /**
     * Overloaded constructor for booking a new ride.
     *
     * @param rideId         Unique identifier for the ride.
     * @param pickupLocation Origin location.
     * @param dropLocation   Destination location.
     * @param vehicle        Selected polymorphic vehicle instance.
     * @param distanceKm     Distance in kilometers.
     * @param driver         Assigned driver.
     * @param user           Booking passenger.
     */
    public Ride(String rideId, String pickupLocation, String dropLocation,
                Vehicle vehicle, double distanceKm, Driver driver, User user) {
        this.rideId = rideId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.vehicle = vehicle;
        this.distanceKm = distanceKm;
        this.driver = driver;
        this.user = user;
        this.fare = (vehicle != null) ? vehicle.calculateFare(distanceKm) : 0.0;
        this.status = "Booked";
        this.rating = 0.0f;
        this.feedback = "";
        this.bookingTime = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(new Date());
        this.isPaid = false;
    }

    /**
     * Fully parameterized constructor for reconstructing historical rides from storage.
     *
     * @param rideId         Unique identifier.
     * @param pickupLocation Origin point.
     * @param dropLocation   Destination point.
     * @param vehicle        Vehicle used.
     * @param distanceKm     Kilometers travelled.
     * @param fare           Total fare amount.
     * @param driver         Driver assigned.
     * @param user           Passenger details.
     * @param status         Final or current lifecycle status.
     * @param rating         Customer rating.
     * @param feedback       Customer feedback text.
     * @param bookingTime    Timestamp string.
     * @param isPaid         Payment status.
     */
    public Ride(String rideId, String pickupLocation, String dropLocation,
                Vehicle vehicle, double distanceKm, double fare, Driver driver,
                User user, String status, float rating, String feedback,
                String bookingTime, boolean isPaid) {
        this.rideId = rideId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.vehicle = vehicle;
        this.distanceKm = distanceKm;
        this.fare = fare;
        this.driver = driver;
        this.user = user;
        this.status = status;
        this.rating = rating;
        this.feedback = feedback;
        this.bookingTime = bookingTime;
        this.isPaid = isPaid;
    }

    // --- Bookable Interface Implementation ---

    @Override
    public void confirmBooking() {
        this.status = "Booked";
    }

    @Override
    public void cancelBooking() {
        this.status = "Cancelled";
    }

    @Override
    public String getBookingStatus() {
        return this.status;
    }

    // --- Payable Interface Implementation ---

    @Override
    public double calculateTotalFare() {
        return this.fare;
    }

    @Override
    public boolean isPaymentCompleted() {
        return this.isPaid;
    }

    @Override
    public void markAsPaid() {
        this.isPaid = true;
    }

    // --- Encapsulation: Getters and Setters ---

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
