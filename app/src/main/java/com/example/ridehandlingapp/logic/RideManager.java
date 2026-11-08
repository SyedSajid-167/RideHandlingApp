package com.example.ridehandlingapp.logic;

import android.content.Context;

import com.example.ridehandlingapp.model.Auto;
import com.example.ridehandlingapp.model.Bike;
import com.example.ridehandlingapp.model.Cab;
import com.example.ridehandlingapp.model.Driver;
import com.example.ridehandlingapp.model.Ride;
import com.example.ridehandlingapp.model.User;
import com.example.ridehandlingapp.model.Vehicle;
import com.example.ridehandlingapp.util.FileStorageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Controller/Manager class coordinating rides, vehicle fleet, and persistence.
 * Demonstrates OOP Concepts:
 * 1. Singleton Design Pattern: Single shared instance throughout app lifecycle.
 * 2. Collections Framework:
 *    - ArrayList<Ride> for maintaining sequential ride history.
 *    - HashMap<String, Ride> for O(1) constant-time ride lookup by ID.
 *    - HashMap<String, Vehicle> as an in-memory vehicle catalog.
 * 3. Encapsulation: Internal structures are private; exposed via controlled methods.
 * 4. Method Overloading: Overloaded assignDriver() and createAndAddRide() methods.
 */
public class RideManager {

    private static RideManager instance;

    private final Context appContext;

    // Collections: ArrayList for ordered ride history
    private final ArrayList<Ride> rideHistoryList;

    // Collections: HashMap for fast O(1) ride lookup by unique ID
    private final HashMap<String, Ride> ridesMap;

    // Collections: HashMap for cataloging available vehicle prototypes
    private final HashMap<String, Vehicle> vehicleCatalog;

    // Random generator for driver selection
    private final Random random;

    /**
     * Private constructor for Singleton Pattern.
     *
     * @param context Application context.
     */
    private RideManager(Context context) {
        this.appContext = (context != null) ? context.getApplicationContext() : null;
        this.rideHistoryList = new ArrayList<>();
        this.ridesMap = new HashMap<>();
        this.vehicleCatalog = new HashMap<>();
        this.random = new Random();

        initVehicleCatalog();
        if (this.appContext != null) {
            loadHistory();
        }
    }

    /**
     * Thread-safe Singleton access method.
     *
     * @param context Android context.
     * @return Shared RideManager instance.
     */
    public static synchronized RideManager getInstance(Context context) {
        if (instance == null) {
            instance = new RideManager(context);
        }
        return instance;
    }

    /**
     * Initializes the vehicle catalog using HashMap.
     */
    private void initVehicleCatalog() {
        vehicleCatalog.put("Bike", new Bike());
        vehicleCatalog.put("Auto", new Auto());
        vehicleCatalog.put("Cab", new Cab());
    }

    /**
     * Loads saved ride history from internal storage into memory Collections.
     */
    public void loadHistory() {
        rideHistoryList.clear();
        ridesMap.clear();

        if (appContext != null) {
            ArrayList<Ride> loadedRides = FileStorageHelper.loadRidesFromFile(appContext);
            for (Ride ride : loadedRides) {
                rideHistoryList.add(ride);
                ridesMap.put(ride.getRideId(), ride);
            }
        }
    }

    /**
     * Persists all current rides to file storage.
     */
    public void saveHistory() {
        if (appContext != null) {
            FileStorageHelper.saveRidesToFile(appContext, rideHistoryList);
        }
    }

    /**
     * Adds a newly booked ride to the collections and persists to storage.
     *
     * @param ride Ride instance to store.
     */
    public void addRide(Ride ride) {
        if (ride != null) {
            // Add to top of list so newest rides appear first in history
            rideHistoryList.add(0, ride);
            ridesMap.put(ride.getRideId(), ride);
            saveHistory();
        }
    }

    /**
     * Updates an existing ride's status or details and saves changes.
     *
     * @param updatedRide Ride instance with updated fields.
     */
    public void updateRide(Ride updatedRide) {
        if (updatedRide == null) {
            return;
        }

        ridesMap.put(updatedRide.getRideId(), updatedRide);
        for (int i = 0; i < rideHistoryList.size(); i++) {
            if (java.util.Objects.equals(rideHistoryList.get(i).getRideId(), updatedRide.getRideId())) {
                rideHistoryList.set(i, updatedRide);
                break;
            }
        }
        saveHistory();
    }

    /**
     * Retrieves a ride by its ID using HashMap lookup.
     *
     * @param rideId Unique ride ID.
     * @return Ride object, or null if not found.
     */
    public Ride getRideById(String rideId) {
        return ridesMap.get(rideId);
    }

    /**
     * Returns the complete ride history list.
     *
     * @return ArrayList of Ride objects.
     */
    public ArrayList<Ride> getRideHistory() {
        return new ArrayList<>(rideHistoryList);
    }

    /**
     * Retrieves a vehicle prototype from the catalog by key.
     *
     * @param vehicleType Vehicle key ("Bike", "Auto", "Cab").
     * @return Vehicle instance.
     */
    public Vehicle getVehicle(String vehicleType) {
        Vehicle vehicle = vehicleCatalog.get(vehicleType);
        return (vehicle != null) ? vehicle : vehicleCatalog.get("Cab");
    }

    /**
     * Method Overloading 1:
     * Assigns a realistic simulated driver based on vehicle category.
     *
     * @param vehicleType Type of vehicle chosen.
     * @return Driver instance.
     */
    public Driver assignDriver(String vehicleType) {
        if ("Bike".equalsIgnoreCase(vehicleType)) {
            String[] names = {"Amit Sharma", "Vikram Rathore", "Sandeep Rawat"};
            String[] bikes = {"Hero Splendor (Black)", "Bajaj Pulsar (Blue)", "TVS Apache (Red)"};
            int idx = random.nextInt(names.length);
            return new Driver("D-" + (100 + random.nextInt(900)), names[idx],
                    "+91 98" + (10000000 + random.nextInt(89999999)),
                    bikes[idx], "DL 03 " + (1000 + random.nextInt(8999)), 4.8);
        } else if ("Auto".equalsIgnoreCase(vehicleType)) {
            String[] names = {"Ramesh Yadav", "Sunil Verma", "Gopal Das"};
            String[] autos = {"Bajaj Compact Auto (Yellow-Green)", "Piaggio Ape Auto (Yellow-Green)"};
            int idx = random.nextInt(names.length);
            return new Driver("D-" + (100 + random.nextInt(900)), names[idx],
                    "+91 97" + (10000000 + random.nextInt(89999999)),
                    autos[idx % autos.length], "DL 1R " + (1000 + random.nextInt(8999)), 4.7);
        } else {
            String[] names = {"Deepak Singh", "Arun Kumar", "Mohit Malhotra", "Praveen Tiwari"};
            String[] cars = {"White Maruti Dzire", "Silver Hyundai Aura", "Grey Honda Amaze"};
            int idx = random.nextInt(names.length);
            return new Driver("D-" + (100 + random.nextInt(900)), names[idx],
                    "+91 96" + (10000000 + random.nextInt(89999999)),
                    cars[idx % cars.length], "DL 01 " + (1000 + random.nextInt(8999)), 4.9);
        }
    }

    /**
     * Method Overloading 2:
     * Assigns a driver with a specific preferred driver name.
     *
     * @param vehicleType         Type of vehicle chosen.
     * @param preferredDriverName Preferred driver's name.
     * @return Driver instance.
     */
    public Driver assignDriver(String vehicleType, String preferredDriverName) {
        Driver driver = assignDriver(vehicleType);
        if (preferredDriverName != null && !preferredDriverName.trim().isEmpty()) {
            driver.setName(preferredDriverName);
        }
        return driver;
    }

    /**
     * Method Overloading 1 for Ride Creation:
     * Creates and adds a ride using default passenger profile.
     *
     * @param pickup      Pickup location string.
     * @param drop        Drop location string.
     * @param vehicleType Vehicle category.
     * @param distance    Distance in km.
     * @return Newly created Ride instance.
     */
    public Ride createAndAddRide(String pickup, String drop, String vehicleType, double distance) {
        User defaultUser = new User("Rahul Verma", "+91 9876501234");
        return createAndAddRide(pickup, drop, vehicleType, distance, defaultUser);
    }

    /**
     * Method Overloading 2 for Ride Creation:
     * Creates and adds a ride for an explicit User.
     *
     * @param pickup      Pickup address.
     * @param drop        Destination address.
     * @param vehicleType Vehicle type.
     * @param distance    Distance in km.
     * @param user        Passenger user.
     * @return Newly created Ride instance.
     */
    public Ride createAndAddRide(String pickup, String drop, String vehicleType, double distance, User user) {
        Vehicle vehicle = getVehicle(vehicleType);
        Driver driver = assignDriver(vehicleType);
        String rideId = "RIDE-" + (1000 + random.nextInt(9000));

        Ride ride = new Ride(rideId, pickup, drop, vehicle, distance, driver, user);
        addRide(ride);
        return ride;
    }
}
