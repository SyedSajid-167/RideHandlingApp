package com.example.ridehandlingapp.util;

import android.content.Context;
import android.util.Log;

import com.example.ridehandlingapp.model.Auto;
import com.example.ridehandlingapp.model.Bike;
import com.example.ridehandlingapp.model.Cab;
import com.example.ridehandlingapp.model.Driver;
import com.example.ridehandlingapp.model.Ride;
import com.example.ridehandlingapp.model.User;
import com.example.ridehandlingapp.model.Vehicle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Utility class handling File I/O for saving and loading ride history.
 * Demonstrates:
 * 1. File I/O using Android Internal Storage (Context.openFileOutput / openFileInput).
 * 2. Exception Handling: Robust try-with-resources and error handling.
 * 3. Persistence: Enables ride history to survive app process termination and restarts.
 */
public class FileStorageHelper {

    private static final String TAG = "FileStorageHelper";
    private static final String FILE_NAME = "rides_history.json";

    /**
     * Saves the list of rides to Android internal storage as JSON.
     *
     * @param context Application context for accessing file system.
     * @param rides   ArrayList of Ride objects to persist.
     * @return True if save succeeded, false otherwise.
     */
    public static boolean saveRidesToFile(Context context, ArrayList<Ride> rides) {
        if (context == null || rides == null) {
            return false;
        }

        try {
            JSONArray jsonArray = new JSONArray();
            for (Ride ride : rides) {
                JSONObject obj = new JSONObject();
                obj.put("rideId", ride.getRideId());
                obj.put("pickupLocation", ride.getPickupLocation());
                obj.put("dropLocation", ride.getDropLocation());

                // Vehicle details
                if (ride.getVehicle() != null) {
                    obj.put("vehicleType", ride.getVehicle().getName());
                    obj.put("baseFare", ride.getVehicle().getBaseFare());
                    obj.put("perKmRate", ride.getVehicle().getPerKmRate());
                } else {
                    obj.put("vehicleType", "Cab");
                    obj.put("baseFare", 60.0);
                    obj.put("perKmRate", 18.0);
                }

                obj.put("distanceKm", ride.getDistanceKm());
                obj.put("fare", ride.getFare());

                // Driver details
                if (ride.getDriver() != null) {
                    obj.put("driverId", ride.getDriver().getDriverId());
                    obj.put("driverName", ride.getDriver().getName());
                    obj.put("driverPhone", ride.getDriver().getPhoneNumber());
                    obj.put("vehicleModel", ride.getDriver().getVehicleModel());
                    obj.put("vehicleNumber", ride.getDriver().getVehicleNumber());
                    obj.put("driverRating", ride.getDriver().getRating());
                }

                // User details
                if (ride.getUser() != null) {
                    obj.put("userId", ride.getUser().getUserId());
                    obj.put("userName", ride.getUser().getName());
                    obj.put("userPhone", ride.getUser().getPhoneNumber());
                    obj.put("userEmail", ride.getUser().getEmail());
                }

                obj.put("status", ride.getStatus());
                obj.put("rating", ride.getRating());
                obj.put("feedback", ride.getFeedback());
                obj.put("bookingTime", ride.getBookingTime());
                obj.put("isPaid", ride.isPaid());

                jsonArray.put(obj);
            }

            // Write to private internal storage using try-with-resources
            try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
                fos.write(jsonArray.toString(2).getBytes(StandardCharsets.UTF_8));
                fos.flush();
                return true;
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Failed to save ride history to internal storage", e);
            return false;
        }
    }

    /**
     * Loads the list of rides from Android internal storage.
     *
     * @param context Application context.
     * @return ArrayList of Ride objects reconstructed from file storage.
     */
    public static ArrayList<Ride> loadRidesFromFile(Context context) {
        ArrayList<Ride> rides = new ArrayList<>();
        if (context == null) {
            return rides;
        }

        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            if (content.length() == 0) {
                return rides;
            }

            JSONArray jsonArray = new JSONArray(content.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String rideId = obj.optString("rideId", "RIDE-0000");
                String pickup = obj.optString("pickupLocation", "");
                String drop = obj.optString("dropLocation", "");
                String vehicleType = obj.optString("vehicleType", "Cab");
                double distance = obj.optDouble("distanceKm", 5.0);
                double fare = obj.optDouble("fare", 100.0);

                // Reconstruct polymorphic Vehicle
                Vehicle vehicle;
                if ("Bike".equalsIgnoreCase(vehicleType)) {
                    vehicle = new Bike();
                } else if ("Auto".equalsIgnoreCase(vehicleType)) {
                    vehicle = new Auto();
                } else {
                    vehicle = new Cab();
                }

                // Reconstruct Driver
                Driver driver = new Driver(
                        obj.optString("driverId", "D-101"),
                        obj.optString("driverName", "Driver"),
                        obj.optString("driverPhone", "+91 9999988888"),
                        obj.optString("vehicleModel", "Sedan"),
                        obj.optString("vehicleNumber", "DL 01 AA 1111"),
                        obj.optDouble("driverRating", 4.8)
                );

                // Reconstruct User
                User user = new User(
                        obj.optString("userId", "U-1"),
                        obj.optString("userName", "Passenger"),
                        obj.optString("userPhone", "+91 9876543210"),
                        obj.optString("userEmail", "passenger@example.com")
                );

                String status = obj.optString("status", "Completed");
                float rating = (float) obj.optDouble("rating", 0.0);
                String feedback = obj.optString("feedback", "");
                String bookingTime = obj.optString("bookingTime", "");
                boolean isPaid = obj.optBoolean("isPaid", true);

                Ride ride = new Ride(rideId, pickup, drop, vehicle, distance, fare,
                        driver, user, status, rating, feedback, bookingTime, isPaid);
                rides.add(ride);
            }
        } catch (IOException e) {
            // File might not exist on first launch, which is completely expected
            Log.i(TAG, "No existing ride history file found (first launch): " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON ride history file", e);
        }

        return rides;
    }
}
