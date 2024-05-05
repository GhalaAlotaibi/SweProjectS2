package com.example.sweprojects2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private List<Staff> staffList;
    private OnStaffClickListener listener;
    private int selectedPosition = -1;

    public StaffAdapter(List<Staff> staffList, OnStaffClickListener listener) {
        this.staffList = staffList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        holder.staffNameText.setText(staff.getStaffName());
        holder.staffSpecialtyText.setText(staff.getSpecialty());

        switch (position) {
            case 0:
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.staff1)
                        .into(holder.staffImage);
                break;
            case 1:
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.staff3)
                        .into(holder.staffImage);
                break;
            case 2:
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.staff5)
                        .into(holder.staffImage);
                break;
            case 3:
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.stst)
                        .into(holder.staffImage);
                break;
            default:
                break;
        }

        // Highlight selected staff member
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFC0CB"));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            selectedPosition = currentPosition;
            notifyDataSetChanged();

            if (listener != null) {
                listener.onStaffClick(staffList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView staffImage;
        TextView staffNameText, staffSpecialtyText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            staffImage = itemView.findViewById(R.id.staffImage);
            staffNameText = itemView.findViewById(R.id.staffNameText);
            staffSpecialtyText = itemView.findViewById(R.id.staffSpecialtyText);
        }
    }

    // Interface for staff click events
    public interface OnStaffClickListener {
        void onStaffClick(Staff staff);
    }
}
