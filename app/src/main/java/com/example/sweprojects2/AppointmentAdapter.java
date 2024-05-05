package com.example.sweprojects2;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<bookO> appointments;
    private DBHelper dbHelper;
    String price;

    public AppointmentAdapter(List<bookO> appointments, DBHelper dbHelper) {
        this.appointments = appointments;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bookO appointment = appointments.get(position);

        String serviceName = appointment.getServiceName();
        holder.serviceNameText.setText(serviceName);
        holder.dateValueText.setText(appointment.getDate());
        if (Objects.equals(serviceName, "Haircut")) {
            price = "$69";
        }
        else if (Objects.equals(serviceName, "Hair Dye")) {
            price = "$49";
        }
        else {
            price = "$99";
        }
        holder.priceText.setText(price);

        if (Objects.equals(serviceName, "Haircut")) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.haircut2)
                    .into(holder.appointmentImage);
        } else if (Objects.equals(serviceName, "Makeup")) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.makeup)
                    .into(holder.appointmentImage);
        } else if (Objects.equals(serviceName, "Hair Dye")) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.hairdye2)
                    .into(holder.appointmentImage);
        }

        // Set click listeners for action buttons
        holder.closeButton.setOnClickListener(v -> {
            // Delete appointment from database
            boolean isDeleted = dbHelper.deleteOneAppointment(appointment);
            if (isDeleted) {
                // Remove appointment from the list and notify the adapter
                appointments.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, appointments.size());
                Toast.makeText(v.getContext(), "Appointment Canceled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Failed to delete appointment", Toast.LENGTH_SHORT).show();
            }
        });

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), book.class);
            intent.putExtra("clientId", appointment.getClientID());
            intent.putExtra("appointmentId", appointment.getAppointmentID());
            intent.putExtra("serviceName", appointment.getServiceName());
            intent.putExtra("staffId", appointment.getStaffID());
            intent.putExtra("date", appointment.getDate());
            intent.putExtra("time", appointment.getTime());
            v.getContext().startActivity(intent);
        });
        holder.previewButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.dialog_appointment_preview);

            TextView serviceNameValueText = dialog.findViewById(R.id.serviceNameValueText);
            TextView staffNameValueText = dialog.findViewById(R.id.staffNameValueText);
            TextView dateValueText = dialog.findViewById(R.id.dateValueText);
            TextView timeValueText = dialog.findViewById(R.id.timeValueText);
            TextView priceValueText = dialog.findViewById(R.id.priceValueText);
            TextView appointmentIDValueText = dialog.findViewById(R.id.appointmentIDValueText);

            serviceNameValueText.setText(appointment.getServiceName());
            staffNameValueText.setText(appointment.getStaffName());
            dateValueText.setText(appointment.getDate());
            timeValueText.setText(appointment.getTime());
            if (Objects.equals(appointment.getServiceName(), "Haircut")) {
                price = "$69";
            }
            else if (Objects.equals(appointment.getServiceName(), "Hair Dye")) {
                price = "$49";
            }
            else {
                price = "$99";
            }
            priceValueText.setText(price);
            appointmentIDValueText.setText(String.valueOf(appointment.getAppointmentID()));

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appointmentImage;
        TextView serviceNameText, dateValueText, priceText;
        ImageView closeButton, editButton, previewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentImage = itemView.findViewById(R.id.appointmentImage);
            serviceNameText = itemView.findViewById(R.id.serviceNameText);
            dateValueText = itemView.findViewById(R.id.dateValueText);
            priceText = itemView.findViewById(R.id.priceText);
            closeButton = itemView.findViewById(R.id.closeButton);
            editButton = itemView.findViewById(R.id.editButton);
            previewButton = itemView.findViewById(R.id.previewButton);
        }
    }

    public void updateAppointments(List<bookO> newAppointments) {
        appointments.clear();
        appointments.addAll(newAppointments);
        notifyDataSetChanged();
    }
}
