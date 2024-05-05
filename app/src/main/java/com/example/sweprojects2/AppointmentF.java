package com.example.sweprojects2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AppointmentF extends AppCompatActivity {

    private RecyclerView appointmentRecyclerView;
    private DBHelper dbHelper;
    private TextView noAppointmentsText;
    private EditText searchEditText;
    private AppointmentAdapter appointmentAdapter;
    int clientId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_appointment_f);

        Intent intent = getIntent();
        clientId = intent.getIntExtra("clientId", -1);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);
        LinearLayout StaffLayout = findViewById(R.id.StaffPage);
        LinearLayout HomeLayout = findViewById(R.id.HomePage);
        LinearLayout ProfileLayout = findViewById(R.id.ProfilePage);

        // Initialize RecyclerView
        appointmentRecyclerView = findViewById(R.id.appointmentRecyclerView);
        appointmentAdapter = new AppointmentAdapter(new ArrayList<>(), dbHelper);
        appointmentRecyclerView.setAdapter(appointmentAdapter);

        noAppointmentsText = findViewById(R.id.noAppointmentsText);

        StaffLayout.setOnClickListener(v -> {
            // Handle click for Staff
            Intent staffIntent = new Intent(AppointmentF.this, StaffPage.class);
            staffIntent.putExtra("clientId", clientId);
            startActivity(staffIntent);
        });

        HomeLayout.setOnClickListener(v -> {
            // Handle click for Appointment
            Intent appointmentsIntent = new Intent(AppointmentF.this, HomePageF.class);
            appointmentsIntent.putExtra("clientId", clientId);
            startActivity(appointmentsIntent);
        });

        /*ProfileLayout.setOnClickListener(v -> {
            // Handle click for Profile
            Intent profileIntent = new Intent(AppointmentF.this, Profile.class);
            startActivity(profileIntent);
        });*/

        searchEditText = findViewById(R.id.editTextText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String searchQuery = s.toString().trim();
                filterAppointments(searchQuery);
            }
        });

        // Load appointments from database and populate RecyclerView
        loadAppointments();
    }

    private void loadAppointments() {
        Intent intent = getIntent();
        int clientId = intent.getIntExtra("clientId", -1);
        List<bookO> appointments = dbHelper.getAllAppointments(clientId);

        if (appointments.isEmpty()) {
            noAppointmentsText.setVisibility(View.VISIBLE);
            appointmentRecyclerView.setVisibility(View.GONE);
        } else {
            noAppointmentsText.setVisibility(View.GONE);
            appointmentRecyclerView.setVisibility(View.VISIBLE);
            appointmentAdapter.updateAppointments(appointments);
            appointmentAdapter.notifyDataSetChanged();
        }
    }

    private void filterAppointments(String searchQuery) {
        List<bookO> filteredAppointments = new ArrayList<>();
        String lowercaseQuery = searchQuery.toLowerCase(); // Convert to lowercase
        if (lowercaseQuery.isEmpty()) {
            filteredAppointments.addAll(dbHelper.getAllAppointments(clientId));
        } else {
            for (bookO appointment : dbHelper.getAllAppointments(clientId)) {
                if (appointment.getServiceName().toLowerCase().contains(lowercaseQuery)) {
                    filteredAppointments.add(appointment);
                }
            }
        }
        appointmentAdapter.updateAppointments(filteredAppointments);
    }
}
