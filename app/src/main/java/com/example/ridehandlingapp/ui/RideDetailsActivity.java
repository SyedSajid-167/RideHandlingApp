package com.example.ridehandlingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ridehandlingapp.R;
import com.example.ridehandlingapp.logic.RideManager;
import com.example.ridehandlingapp.model.Driver;
import com.example.ridehandlingapp.model.Ride;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ride Details and Live Tracking Screen.
 * Demonstrates:
 * 1. Multithreading: Background thread simulation with ExecutorService.
 * 2. Thread Safety and UI Synchronization: runOnUiThread() for safe UI updates.
 * 3. Lifecycle State Management: Allows user cancellation during active simulation.
 * 4. Encapsulation: Interacts with Ride model through getters and setters.
 */
public class RideDetailsActivity extends AppCompatActivity {

    private TextView tvRideIdHeader;
    private TextView tvStatusBadge;
    private TextView tvStatusDescription;
    private TextView tvSimIndicator;
    private ProgressBar pbRideProgress;

    private TextView tvDriverName;
    private TextView tvDriverPhone;
    private TextView tvDriverRating;
    private TextView tvVehicleModel;
    private TextView tvVehiclePlate;

    private TextView tvDetailsPickup;
    private TextView tvDetailsDrop;
    private TextView tvDetailsDistance;
    private TextView tvDetailsFare;

    private MaterialButton btnCancelRide;
    private MaterialButton btnDoneRide;

    private RideManager rideManager;
    private Ride currentRide;
    private ExecutorService threadExecutor;
    private final AtomicBoolean isCancelled = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        rideManager = RideManager.getInstance(this);

        String rideId = getIntent().getStringExtra(MainActivity.EXTRA_RIDE_ID);
        currentRide = rideManager.getRideById(rideId);

        if (currentRide == null) {
            Toast.makeText(this, "Error: Ride not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        populateRideData();
        setupActions();

        // Start Multithreaded live simulation if ride is freshly booked
        if ("Booked".equalsIgnoreCase(currentRide.getStatus())) {
            startRideSimulationThread();
        } else {
            // Already in a settled status (e.g. from history inspection)
            updateUIForStatus(currentRide.getStatus());
        }
    }

    /**
     * Binds UI widgets to layout IDs.
     */
    private void initViews() {
        tvRideIdHeader = findViewById(R.id.tvRideIdHeader);
        tvStatusBadge = findViewById(R.id.tvStatusBadge);
        tvStatusDescription = findViewById(R.id.tvStatusDescription);
        tvSimIndicator = findViewById(R.id.tvSimIndicator);
        pbRideProgress = findViewById(R.id.pbRideProgress);

        tvDriverName = findViewById(R.id.tvDriverName);
        tvDriverPhone = findViewById(R.id.tvDriverPhone);
        tvDriverRating = findViewById(R.id.tvDriverRating);
        tvVehicleModel = findViewById(R.id.tvVehicleModel);
        tvVehiclePlate = findViewById(R.id.tvVehiclePlate);

        tvDetailsPickup = findViewById(R.id.tvDetailsPickup);
        tvDetailsDrop = findViewById(R.id.tvDetailsDrop);
        tvDetailsDistance = findViewById(R.id.tvDetailsDistance);
        tvDetailsFare = findViewById(R.id.tvDetailsFare);

        btnCancelRide = findViewById(R.id.btnCancelRide);
        btnDoneRide = findViewById(R.id.btnDoneRide);
    }

    /**
     * Fills views with current Ride object data.
     */
    private void populateRideData() {
        tvRideIdHeader.setText(currentRide.getRideId());
        tvDetailsPickup.setText(currentRide.getPickupLocation());
        tvDetailsDrop.setText(currentRide.getDropLocation());
        tvDetailsDistance.setText(String.format(Locale.getDefault(), "Distance: %.1f km", currentRide.getDistanceKm()));
        tvDetailsFare.setText(String.format(Locale.getDefault(), "Fare: ₹ %.0f", currentRide.getFare()));

        Driver driver = currentRide.getDriver();
        if (driver != null) {
            tvDriverName.setText(driver.getName());
            tvDriverPhone.setText(driver.getPhoneNumber());
            tvDriverRating.setText(String.format(Locale.getDefault(), "★ %.1f", driver.getRating()));
            tvVehicleModel.setText(driver.getVehicleModel());
            tvVehiclePlate.setText(driver.getVehicleNumber());
        }
    }

    /**
     * Configures button listeners.
     */
    private void setupActions() {
        btnCancelRide.setOnClickListener(v -> cancelCurrentRide());

        btnDoneRide.setOnClickListener(v -> {
            if ("Completed".equalsIgnoreCase(currentRide.getStatus())) {
                Intent intent = new Intent(RideDetailsActivity.this, RatingActivity.class);
                intent.putExtra(MainActivity.EXTRA_RIDE_ID, currentRide.getRideId());
                startActivity(intent);
            }
            finish();
        });
    }

    /**
     * Multithreading Feature:
     * Spawns a background thread that simulates the real-time progression of the ride.
     * Transitions: Booked -> Driver Arriving (3.5s) -> Ongoing (4.0s) -> Completed (4.5s).
     * Synchronizes with UI thread using runOnUiThread().
     */
    private void startRideSimulationThread() {
        threadExecutor = Executors.newSingleThreadExecutor();

        threadExecutor.execute(() -> {
            try {
                // Stage 1: Booked state
                runOnUiThread(() -> {
                    updateUIForStatus("Booked");
                    pbRideProgress.setProgress(20);
                });
                Thread.sleep(3500);

                if (isCancelled.get() || isFinishing()) return;

                // Stage 2: Driver Arriving
                runOnUiThread(() -> {
                    currentRide.setStatus("Driver Arriving");
                    rideManager.updateRide(currentRide);
                    updateUIForStatus("Driver Arriving");
                    pbRideProgress.setProgress(50);
                });
                Thread.sleep(4000);

                if (isCancelled.get() || isFinishing()) return;

                // Stage 3: Ongoing Ride
                runOnUiThread(() -> {
                    currentRide.setStatus("Ongoing");
                    rideManager.updateRide(currentRide);
                    updateUIForStatus("Ongoing");
                    pbRideProgress.setProgress(80);
                });
                Thread.sleep(4500);

                if (isCancelled.get() || isFinishing()) return;

                // Stage 4: Completed Ride
                runOnUiThread(() -> {
                    currentRide.setStatus("Completed");
                    currentRide.markAsPaid();
                    rideManager.updateRide(currentRide);
                    updateUIForStatus("Completed");
                    pbRideProgress.setProgress(100);
                    Toast.makeText(RideDetailsActivity.this, "Ride Completed! Destination reached.", Toast.LENGTH_SHORT).show();
                });

            } catch (InterruptedException e) {
                // Simulation interrupted due to user cancellation or activity exit
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Updates the status card UI widgets and colors based on lifecycle status.
     *
     * @param status Current status string.
     */
    private void updateUIForStatus(String status) {
        tvStatusBadge.setText(status);

        if ("Booked".equalsIgnoreCase(status)) {
            tvStatusBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.badge_status_booked));
            tvStatusDescription.setText("Booking confirmed. Dispatching nearest driver...");
            btnCancelRide.setVisibility(View.VISIBLE);
            btnDoneRide.setEnabled(false);
            btnDoneRide.setText("Ride in Progress...");
            tvSimIndicator.setVisibility(View.VISIBLE);
        } else if ("Driver Arriving".equalsIgnoreCase(status)) {
            tvStatusBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.badge_status_arriving));
            String driverName = currentRide.getDriver() != null ? currentRide.getDriver().getName() : "Driver";
            tvStatusDescription.setText(driverName + " is arriving at your pickup spot. Please be ready!");
            btnCancelRide.setVisibility(View.VISIBLE);
            btnDoneRide.setEnabled(false);
            btnDoneRide.setText("Driver Arriving...");
            tvSimIndicator.setVisibility(View.VISIBLE);
        } else if ("Ongoing".equalsIgnoreCase(status)) {
            tvStatusBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.badge_status_ongoing));
            tvStatusDescription.setText("Ride underway to destination: " + currentRide.getDropLocation());
            btnCancelRide.setVisibility(View.GONE); // Once ride begins, cancel disabled
            btnDoneRide.setEnabled(false);
            btnDoneRide.setText("Ride in Progress...");
            tvSimIndicator.setVisibility(View.VISIBLE);
        } else if ("Completed".equalsIgnoreCase(status)) {
            tvStatusBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.badge_status_completed));
            tvStatusDescription.setText("You have safely reached your destination. Thank you for riding!");
            btnCancelRide.setVisibility(View.GONE);
            btnDoneRide.setEnabled(true);
            btnDoneRide.setText("Rate Driver & Finish");
            tvSimIndicator.setVisibility(View.GONE);
        } else if ("Cancelled".equalsIgnoreCase(status)) {
            tvStatusBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.badge_status_cancelled));
            tvStatusDescription.setText("This ride was cancelled by the passenger.");
            pbRideProgress.setProgress(0);
            btnCancelRide.setVisibility(View.GONE);
            btnDoneRide.setEnabled(true);
            btnDoneRide.setText("Return to Home");
            tvSimIndicator.setVisibility(View.GONE);
        }
    }

    /**
     * Handles ride cancellation by user.
     */
    private void cancelCurrentRide() {
        isCancelled.set(true);
        if (threadExecutor != null) {
            threadExecutor.shutdownNow();
        }

        currentRide.cancelBooking(); // Invokes Bookable interface method
        rideManager.updateRide(currentRide); // Persists to storage
        updateUIForStatus("Cancelled");

        Toast.makeText(this, "Ride cancelled.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCancelled.set(true);
        if (threadExecutor != null && !threadExecutor.isShutdown()) {
            threadExecutor.shutdownNow();
        }
    }
}
