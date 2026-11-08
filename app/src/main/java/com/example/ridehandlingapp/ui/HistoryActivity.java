package com.example.ridehandlingapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridehandlingapp.R;
import com.example.ridehandlingapp.logic.RideManager;
import com.example.ridehandlingapp.model.Ride;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Ride History Activity.
 * Demonstrates:
 * 1. RecyclerView implementation with custom adapter.
 * 2. File I/O persistence loading from Android internal storage.
 * 3. Collection iteration (ArrayList<Ride>) to calculate summary metrics.
 * 4. User interface state management for empty and populated states.
 */
public class HistoryActivity extends AppCompatActivity {

    private MaterialToolbar toolbarHistory;
    private TextView tvTotalRidesCount;
    private TextView tvTotalSpent;
    private LinearLayout layoutEmptyHistory;
    private RecyclerView rvRideHistory;

    private RideManager rideManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rideManager = RideManager.getInstance(this);

        initViews();
        setupToolbar();
        loadHistoryData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh history upon return (in case rides were rated or status updated)
        loadHistoryData();
    }

    /**
     * Binds UI widgets.
     */
    private void initViews() {
        toolbarHistory = findViewById(R.id.toolbarHistory);
        tvTotalRidesCount = findViewById(R.id.tvTotalRidesCount);
        tvTotalSpent = findViewById(R.id.tvTotalSpent);
        layoutEmptyHistory = findViewById(R.id.layoutEmptyHistory);
        rvRideHistory = findViewById(R.id.rvRideHistory);

        rvRideHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Configures the top toolbar navigation.
     */
    private void setupToolbar() {
        toolbarHistory.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Fetches ride history from RideManager and binds to the RecyclerView.
     */
    private void loadHistoryData() {
        // Reload from file storage to guarantee freshest state
        rideManager.loadHistory();
        ArrayList<Ride> rideHistoryList = rideManager.getRideHistory();

        if (rideHistoryList == null || rideHistoryList.isEmpty()) {
            layoutEmptyHistory.setVisibility(View.VISIBLE);
            rvRideHistory.setVisibility(View.GONE);
            tvTotalRidesCount.setText("0 Rides");
            tvTotalSpent.setText("₹ 0");
        } else {
            layoutEmptyHistory.setVisibility(View.GONE);
            rvRideHistory.setVisibility(View.VISIBLE);

            // Compute summary statistics across the ArrayList
            double totalSpent = 0;
            for (Ride r : rideHistoryList) {
                if (!"Cancelled".equalsIgnoreCase(r.getStatus())) {
                    totalSpent += r.getFare();
                }
            }

            tvTotalRidesCount.setText(String.format(Locale.getDefault(), "%d Rides", rideHistoryList.size()));
            tvTotalSpent.setText(String.format(Locale.getDefault(), "₹ %.0f", totalSpent));

            RideHistoryAdapter adapter = new RideHistoryAdapter(this, rideHistoryList);
            rvRideHistory.setAdapter(adapter);
        }
    }
}
