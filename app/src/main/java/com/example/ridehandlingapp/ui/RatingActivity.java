package com.example.ridehandlingapp.ui;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridehandlingapp.R;
import com.example.ridehandlingapp.logic.RideManager;
import com.example.ridehandlingapp.model.Driver;
import com.example.ridehandlingapp.model.Ride;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

/**
 * Rating & Feedback Activity.
 * Demonstrates:
 * 1. Post-trip rating and feedback capture.
 * 2. Model Encapsulation: Modifies Ride object via setRating() and setFeedback().
 * 3. File Persistence: Persists updated feedback into internal storage through RideManager.
 */
public class RatingActivity extends AppCompatActivity {

    private TextView tvRatingDriverName;
    private TextView tvRatingFare;
    private TextView tvRatingVehicleInfo;
    private RatingBar rbDriverRating;
    private TextView tvRatingFeedbackLabel;
    private TextInputEditText etFeedbackComment;
    private MaterialButton btnSubmitRating;
    private MaterialButton btnSkipRating;

    private RideManager rideManager;
    private Ride currentRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        rideManager = RideManager.getInstance(this);

        String rideId = getIntent().getStringExtra(MainActivity.EXTRA_RIDE_ID);
        currentRide = rideManager.getRideById(rideId);

        if (currentRide == null) {
            Toast.makeText(this, "Ride details unavailable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        populateSummary();
        setupListeners();
    }

    /**
     * Binds UI widgets.
     */
    private void initViews() {
        tvRatingDriverName = findViewById(R.id.tvRatingDriverName);
        tvRatingFare = findViewById(R.id.tvRatingFare);
        tvRatingVehicleInfo = findViewById(R.id.tvRatingVehicleInfo);
        rbDriverRating = findViewById(R.id.rbDriverRating);
        tvRatingFeedbackLabel = findViewById(R.id.tvRatingFeedbackLabel);
        etFeedbackComment = findViewById(R.id.etFeedbackComment);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);
        btnSkipRating = findViewById(R.id.btnSkipRating);
    }

    /**
     * Fills the trip summary card.
     */
    private void populateSummary() {
        Driver driver = currentRide.getDriver();
        if (driver != null) {
            tvRatingDriverName.setText(String.format("Driver: %s", driver.getName()));
            String vehicleInfo = String.format("%s (%s) • %s",
                    driver.getVehicleModel(),
                    currentRide.getVehicle() != null ? currentRide.getVehicle().getName() : "Cab",
                    driver.getVehicleNumber());
            tvRatingVehicleInfo.setText(vehicleInfo);
        }

        tvRatingFare.setText(String.format(Locale.getDefault(), "₹ %.0f", currentRide.getFare()));
    }

    /**
     * Configures RatingBar and Button click handlers.
     */
    private void setupListeners() {
        rbDriverRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            int stars = Math.round(rating);
            switch (stars) {
                case 1:
                    tvRatingFeedbackLabel.setText("Poor (1 Star)");
                    break;
                case 2:
                    tvRatingFeedbackLabel.setText("Fair (2 Stars)");
                    break;
                case 3:
                    tvRatingFeedbackLabel.setText("Good (3 Stars)");
                    break;
                case 4:
                    tvRatingFeedbackLabel.setText("Very Good (4 Stars)");
                    break;
                case 5:
                default:
                    tvRatingFeedbackLabel.setText("Excellent (5 Stars)");
                    break;
            }
        });

        btnSubmitRating.setOnClickListener(v -> {
            float rating = rbDriverRating.getRating();
            if (rating <= 0) {
                Toast.makeText(this, "Please select at least 1 star.", Toast.LENGTH_SHORT).show();
                return;
            }

            String comment = (etFeedbackComment.getText() != null)
                    ? etFeedbackComment.getText().toString().trim() : "";

            // Update encapsulated ride fields
            currentRide.setRating(rating);
            currentRide.setFeedback(comment);

            // Save to internal storage via RideManager
            rideManager.updateRide(currentRide);

            Toast.makeText(this, "Thank you for your rating!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnSkipRating.setOnClickListener(v -> finish());
    }
}
