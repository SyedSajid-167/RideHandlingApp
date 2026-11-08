package com.example.ridehandlingapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridehandlingapp.R;
import com.example.ridehandlingapp.model.Ride;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Custom RecyclerView Adapter for rendering the ride history list.
 * Demonstrates:
 * 1. Adapter Pattern: Bridges Collection data (ArrayList<Ride>) with UI Views.
 * 2. Encapsulation: ViewHolder encapsulates individual row UI elements.
 * 3. Dynamic UI state binding based on ride status.
 */
public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.RideViewHolder> {

    private final Context context;
    private final ArrayList<Ride> rideList;

    /**
     * Parameterized constructor initializing data source.
     *
     * @param context  Android context for inflating resources.
     * @param rideList List of Ride objects to display.
     */
    public RideHistoryAdapter(Context context, ArrayList<Ride> rideList) {
        this.context = context;
        this.rideList = rideList != null ? rideList : new ArrayList<>();
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ride_history, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride ride = rideList.get(position);
        if (ride == null) {
            return;
        }

        holder.tvRideId.setText(ride.getRideId());
        holder.tvDate.setText(ride.getBookingTime());
        holder.tvPickup.setText(ride.getPickupLocation());
        holder.tvDrop.setText(ride.getDropLocation());

        String fareText = String.format(Locale.getDefault(), "₹ %.0f", ride.getFare());
        holder.tvFare.setText(fareText);

        String vehicleName = (ride.getVehicle() != null) ? ride.getVehicle().getName() : "Vehicle";
        String driverName = (ride.getDriver() != null) ? ride.getDriver().getName() : "Driver";
        String vehicleInfo = vehicleName + " • " + driverName;
        holder.tvVehicle.setText(vehicleInfo);

        // Status badge configuration
        String status = ride.getStatus();
        holder.tvStatusBadge.setText(status);

        if ("Completed".equalsIgnoreCase(status)) {
            holder.tvStatusBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_status_completed));
        } else if ("Cancelled".equalsIgnoreCase(status)) {
            holder.tvStatusBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_status_cancelled));
        } else if ("Driver Arriving".equalsIgnoreCase(status)) {
            holder.tvStatusBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_status_arriving));
        } else if ("Ongoing".equalsIgnoreCase(status)) {
            holder.tvStatusBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_status_ongoing));
        } else {
            holder.tvStatusBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_status_booked));
        }

        // Rating display
        if (ride.getRating() > 0) {
            holder.tvRating.setVisibility(View.VISIBLE);
            holder.tvRating.setText(String.format(Locale.getDefault(), "★ %.1f", ride.getRating()));
        } else {
            holder.tvRating.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }

    /**
     * ViewHolder class holding direct references to UI widgets.
     */
    public static class RideViewHolder extends RecyclerView.ViewHolder {

        TextView tvRideId;
        TextView tvDate;
        TextView tvStatusBadge;
        TextView tvPickup;
        TextView tvDrop;
        TextView tvVehicle;
        TextView tvRating;
        TextView tvFare;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRideId = itemView.findViewById(R.id.tvItemRideId);
            tvDate = itemView.findViewById(R.id.tvItemDate);
            tvStatusBadge = itemView.findViewById(R.id.tvItemStatusBadge);
            tvPickup = itemView.findViewById(R.id.tvItemPickup);
            tvDrop = itemView.findViewById(R.id.tvItemDrop);
            tvVehicle = itemView.findViewById(R.id.tvItemVehicle);
            tvRating = itemView.findViewById(R.id.tvItemRating);
            tvFare = itemView.findViewById(R.id.tvItemFare);
        }
    }
}
