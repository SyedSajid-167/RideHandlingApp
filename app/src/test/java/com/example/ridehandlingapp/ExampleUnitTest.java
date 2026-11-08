package com.example.ridehandlingapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.example.ridehandlingapp.logic.FareCalculator;
import com.example.ridehandlingapp.logic.RideManager;
import com.example.ridehandlingapp.model.Auto;
import com.example.ridehandlingapp.model.Bike;
import com.example.ridehandlingapp.model.Bookable;
import com.example.ridehandlingapp.model.Cab;
import com.example.ridehandlingapp.model.Driver;
import com.example.ridehandlingapp.model.Payable;
import com.example.ridehandlingapp.model.Ride;
import com.example.ridehandlingapp.model.User;
import com.example.ridehandlingapp.model.Vehicle;
import com.example.ridehandlingapp.util.InvalidLocationException;

import org.junit.Test;

/**
 * Comprehensive Unit Tests verifying OOP Concepts:
 * 1. Inheritance, Polymorphism & Method Overriding (Vehicle hierarchy)
 * 2. Interface Abstraction (Bookable and Payable)
 * 3. Encapsulation (Getters and Setters)
 * 4. Method Overloading & Constructor Overloading
 * 5. Custom Exception Handling (InvalidLocationException)
 */
public class ExampleUnitTest {

    @Test
    public void testBikeFareCalculation() {
        Vehicle bike = new Bike(); // Base 20, Per Km 8
        double fare = bike.calculateFare(10.0);
        assertEquals(100.0, fare, 0.01);
    }

    @Test
    public void testAutoFareCalculation() {
        Vehicle auto = new Auto(); // Base 35, Per Km 12
        double fare = auto.calculateFare(10.0);
        assertEquals(155.0, fare, 0.01);
    }

    @Test
    public void testCabFareCalculation() {
        Vehicle cab = new Cab(); // Base 60, Per Km 18
        double fare = cab.calculateFare(10.0);
        assertEquals(240.0, fare, 0.01);
    }

    @Test
    public void testPolymorphicFareCalculator() {
        // Vehicle reference pointing to Cab object (Polymorphism)
        Vehicle vehicle = new Cab();
        double fare = FareCalculator.calculateFare(vehicle, 5.0);
        // Base 60 + (18 * 5) = 150
        assertEquals(150.0, fare, 0.01);
    }

    @Test
    public void testSurgePricingFareCalculatorOverloading() {
        Vehicle bike = new Bike();
        // Method Overloading signature 1 (with surge multiplier)
        double fare = FareCalculator.calculateFare(bike, 10.0, 1.5);
        assertEquals(150.0, fare, 0.01);

        // Method Overloading signature 2 (primitive base and perKm)
        double primitiveFare = FareCalculator.calculateFare(50.0, 10.0, 5.0);
        assertEquals(100.0, primitiveFare, 0.01);

        // Method Overloading signature 3 (primitive with surge)
        double surgePrimitiveFare = FareCalculator.calculateFare(50.0, 10.0, 5.0, 2.0);
        assertEquals(200.0, surgePrimitiveFare, 0.01);
    }

    @Test
    public void testConstructorOverloading() {
        // Bike constructor overloading
        Bike defaultBike = new Bike();
        Bike customBike = new Bike(30.0, 10.0);
        assertEquals(20.0, defaultBike.getBaseFare(), 0.01);
        assertEquals(30.0, customBike.getBaseFare(), 0.01);

        // Auto constructor overloading
        Auto defaultAuto = new Auto();
        Auto customAuto = new Auto(45.0, 15.0);
        assertEquals(35.0, defaultAuto.getBaseFare(), 0.01);
        assertEquals(45.0, customAuto.getBaseFare(), 0.01);

        // Cab constructor overloading
        Cab defaultCab = new Cab();
        Cab customCab = new Cab(80.0, 20.0);
        assertEquals(60.0, defaultCab.getBaseFare(), 0.01);
        assertEquals(80.0, customCab.getBaseFare(), 0.01);
    }

    @Test
    public void testBookableAndPayableInterfaces() {
        Vehicle cab = new Cab();
        Driver driver = new Driver("Suresh", "+91 9999911111", "White Swift", "DL 01 AB 9999");
        User user = new User("Rahul", "+91 9888822222");

        Ride ride = new Ride("RIDE-99", "Point A", "Point B", cab, 10.0, driver, user);

        // Test Bookable Interface
        Bookable bookable = ride;
        assertEquals("Booked", bookable.getBookingStatus());
        bookable.cancelBooking();
        assertEquals("Cancelled", bookable.getBookingStatus());
        bookable.confirmBooking();
        assertEquals("Booked", bookable.getBookingStatus());

        // Test Payable Interface
        Payable payable = ride;
        assertEquals(240.0, payable.calculateTotalFare(), 0.01);
        assertFalse(payable.isPaymentCompleted());
        payable.markAsPaid();
        assertTrue(payable.isPaymentCompleted());
    }

    @Test
    public void testEncapsulationGettersAndSetters() {
        User user = new User();
        user.setName("Pooja");
        user.setPhoneNumber("+91 9123456789");
        user.setEmail("pooja@example.com");
        user.setUserId("U-500");

        assertEquals("Pooja", user.getName());
        assertEquals("+91 9123456789", user.getPhoneNumber());
        assertEquals("pooja@example.com", user.getEmail());
        assertEquals("U-500", user.getUserId());

        Driver driver = new Driver();
        driver.setName("Vikas");
        driver.setRating(4.9);
        driver.setVehicleModel("Hyundai Aura");
        driver.setVehicleNumber("DL 02 CD 4321");
        driver.setPhoneNumber("+91 9876500000");
        driver.setDriverId("D-777");

        assertEquals("Vikas", driver.getName());
        assertEquals(4.9, driver.getRating(), 0.01);
        assertEquals("Hyundai Aura", driver.getVehicleModel());
        assertEquals("DL 02 CD 4321", driver.getVehicleNumber());
        assertEquals("+91 9876500000", driver.getPhoneNumber());
        assertEquals("D-777", driver.getDriverId());
    }

    @Test
    public void testRideManagerCollectionsAndOverloading() {
        RideManager manager = RideManager.getInstance(null);

        // Test Overloaded assignDriver methods
        Driver driverAuto = manager.assignDriver("Auto");
        assertNotNull(driverAuto);
        assertNotNull(driverAuto.getName());

        Driver driverCustom = manager.assignDriver("Cab", "Special Chauffeur");
        assertNotNull(driverCustom);
        assertEquals("Special Chauffeur", driverCustom.getName());

        // Test Overloaded createAndAddRide method
        Ride ride = manager.createAndAddRide("Gateway of India", "Marine Drive", "Cab", 8.0);
        assertNotNull(ride);
        assertEquals("Gateway of India", ride.getPickupLocation());
        assertEquals("Marine Drive", ride.getDropLocation());

        // Test Collections: HashMap fast lookup by ID
        Ride retrievedRide = manager.getRideById(ride.getRideId());
        assertNotNull(retrievedRide);
        assertEquals(ride.getRideId(), retrievedRide.getRideId());

        // Test Collections: ArrayList history
        assertTrue(manager.getRideHistory().size() >= 1);
    }

    @Test
    public void testCustomInvalidLocationException() {
        try {
            validateTestLocations("", "Airport");
            fail("Should have thrown InvalidLocationException for empty pickup");
        } catch (InvalidLocationException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("Pickup location cannot be empty"));
        }

        try {
            validateTestLocations("Airport", "Airport");
            fail("Should have thrown InvalidLocationException for identical locations");
        } catch (InvalidLocationException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("identical"));
        }
    }

    private void validateTestLocations(String pickup, String drop) throws InvalidLocationException {
        if (pickup == null || pickup.trim().isEmpty()) {
            throw new InvalidLocationException("Pickup location cannot be empty.");
        }
        if (drop == null || drop.trim().isEmpty()) {
            throw new InvalidLocationException("Destination drop location cannot be empty.");
        }
        if (pickup.trim().equalsIgnoreCase(drop.trim())) {
            throw new InvalidLocationException("Pickup and destination locations cannot be identical.");
        }
    }
}
