package com.example.sweprojects2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    private List<String> timeSlots;
    private OnTimeSlotClickListener listener;
    private int selectedPosition = -1;

    public TimeSlotAdapter(List<String> timeSlots, OnTimeSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String timeSlot = timeSlots.get(position);
        holder.timeSlotText.setText(timeSlot);

        // Highlight selected time slot
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFC0CB"));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> {
            // Update selected position and notify adapter
            int currentPosition = holder.getAdapterPosition();
            selectedPosition = currentPosition;
            notifyDataSetChanged();

            if (listener != null) {
                listener.onTimeSlotClick(timeSlot);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeSlotText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlotText = itemView.findViewById(R.id.timeSlotText);
        }
    }

    // Interface for time slot click events
    public interface OnTimeSlotClickListener {
        void onTimeSlotClick(String timeSlot);
    }
}