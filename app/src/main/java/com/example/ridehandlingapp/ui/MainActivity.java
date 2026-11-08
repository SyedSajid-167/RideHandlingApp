package com.example.ridehandlingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridehandlingapp.R;
import com.example.ridehandlingapp.logic.FareCalculator;
import com.example.ridehandlingapp.logic.RideManager;
import com.example.ridehandlingapp.model.Ride;
import com.example.ridehandlingapp.model.Vehicle;
import com.example.ridehandlingapp.util.InvalidLocationException;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

/**
 * Home Screen Activity for booking rides.
 * Demonstrates:
 * 1. Exception Handling: Custom InvalidLocationException thrown and caught with try-catch.
 * 2. Polymorphic Fare Calculation via FareCalculator and Vehicle subclasses.
 * 3. Activity Lifecycle and Navigation using Android Intents.
 * 4. Input validation and user feedback.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RIDE_ID = "com.example.ridehandlingapp.EXTRA_RIDE_ID";

    private TextInputEditText etPickup;
    private TextInputEditText etDrop;
    private RadioGroup rgVehicles;
    private RadioButton rbBike;
    private RadioButton rbAuto;
    private TextView tvDistanceEstimate;
    private TextView tvEstimatedFare;
    private MaterialButton btnBookRide;
    private MaterialButton btnViewHistory;

    private RideManager rideManager;
    private double currentDistanceKm = 12.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize logic manager singleton
        rideManager = RideManager.getInstance(this);

        initViews();
        setupListeners();
        updateFareEstimate();
    }

    /**
     * Binds UI widget references from layout XML.
     */
    private void initViews() {
        etPickup = findViewById(R.id.etPickup);
        etDrop = findViewById(R.id.etDrop);
        rgVehicles = findViewById(R.id.rgVehicles);
        rbBike = findViewById(R.id.rbBike);
        rbAuto = findViewById(R.id.rbAuto);
        tvDistanceEstimate = findViewById(R.id.tvDistanceEstimate);
        tvEstimatedFare = findViewById(R.id.tvEstimatedFare);
        btnBookRide = findViewById(R.id.btnBookRide);
        btnViewHistory = findViewById(R.id.btnViewHistory);
    }

    /**
     * Configures input listeners for interactive updates.
     */
    private void setupListeners() {
        TextWatcher locationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recomputeDistance();
                updateFareEstimate();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        etPickup.addTextChangedListener(locationWatcher);
        etDrop.addTextChangedListener(locationWatcher);

        rgVehicles.setOnCheckedChangeListener((group, checkedId) -> updateFareEstimate());

        btnBookRide.setOnClickListener(v -> {
            hideKeyboard();
            attemptRideBooking();
        });

        btnViewHistory.setOnClickListener(v -> {
            hideKeyboard();
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Hides the software keyboard and clears focus.
     */
    private void hideKeyboard() {
        android.view.View view = getCurrentFocus();
        if (view != null) {
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            view.clearFocus();
        }
    }

    /**
     * Determines selected vehicle type string from radio options.
     *
     * @return "Bike", "Auto", or "Cab".
     */
    private String getSelectedVehicleType() {
        if (rbBike.isChecked()) {
            return "Bike";
        } else if (rbAuto.isChecked()) {
            return "Auto";
        } else {
            return "Cab";
        }
    }

    /**
     * Recomputes simulated distance based on pickup and drop string inputs.
     */
    private void recomputeDistance() {
        String pickup = (etPickup.getText() != null) ? etPickup.getText().toString().trim() : "";
        String drop = (etDrop.getText() != null) ? etDrop.getText().toString().trim() : "";

        if (pickup.isEmpty() || drop.isEmpty()) {
            currentDistanceKm = 5.0;
        } else {
            // Deterministic calculation based on string character lengths for realistic variance
            int lengthSum = pickup.length() + drop.length();
            double simulatedDist = 3.0 + ((lengthSum % 25) * 0.7);
            currentDistanceKm = Math.round(simulatedDist * 10.0) / 10.0;
        }

        tvDistanceEstimate.setText(String.format(Locale.getDefault(), "Est. Distance: ~%.1f km", currentDistanceKm));
    }

    /**
     * Updates the estimated fare badge using polymorphic calculation.
     */
    private void updateFareEstimate() {
        String vehicleType = getSelectedVehicleType();
        Vehicle vehicle = rideManager.getVehicle(vehicleType);

        // Polymorphic fare calculation: calls overridden method in selected subclass
        double estimatedFare = FareCalculator.calculateFare(vehicle, currentDistanceKm);
        tvEstimatedFare.setText(String.format(Locale.getDefault(), "₹ %.0f", estimatedFare));
    }

    /**
     * Validates user inputs and handles custom InvalidLocationException.
     * Demonstrates Exception Handling with try-catch.
     */
    private void validateLocations(String pickup, String drop) throws InvalidLocationException {
        if (pickup == null || pickup.trim().isEmpty()) {
            throw new InvalidLocationException("Pickup location cannot be empty.");
        }
        if (drop == null || drop.trim().isEmpty()) {
            throw new InvalidLocationException("Destination drop location cannot be empty.");
        }
        if (pickup.trim().length() < 3) {
            throw new InvalidLocationException("Pickup address is too short (min 3 characters).");
        }
        if (drop.trim().length() < 3) {
            throw new InvalidLocationException("Destination address is too short (min 3 characters).");
        }
        if (pickup.trim().equalsIgnoreCase(drop.trim())) {
            throw new InvalidLocationException("Pickup and destination locations cannot be identical.");
        }
    }

    /**
     * Attempts booking a ride with try-catch block for custom exception.
     */
    private void attemptRideBooking() {
        String pickup = (etPickup.getText() != null) ? etPickup.getText().toString().trim() : "";
        String drop = (etDrop.getText() != null) ? etDrop.getText().toString().trim() : "";
        String vehicleType = getSelectedVehicleType();

        try {
            // Validate input: throws custom InvalidLocationException on failure
            validateLocations(pickup, drop);

            // Create and store ride in RideManager (demonstrating Collections and File Storage)
            Ride newRide = rideManager.createAndAddRide(pickup, drop, vehicleType, currentDistanceKm);

            Toast.makeText(this, "Ride booked successfully! Connecting driver...", Toast.LENGTH_SHORT).show();

            // Transition to Ride Details screen
            Intent intent = new Intent(MainActivity.this, RideDetailsActivity.class);
            intent.putExtra(EXTRA_RIDE_ID, newRide.getRideId());
            startActivity(intent);

        } catch (InvalidLocationException e) {
            // Exception Handling: Catching custom exception and displaying user-friendly feedback
            Toast.makeText(this, "Validation Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
