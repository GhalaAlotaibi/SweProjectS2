package com.example.sweprojects2;


import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageF extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_home_page_f);

        // Get the client ID passed from the intent
        Intent intent = getIntent();
        int clientId = intent.getIntExtra("clientId", -1);

        // Get the client's name from the database using the client ID
        String clientName = getClientName(clientId);

        // Find the TextView for the welcome message
        TextView welcomeTextView = findViewById(R.id.textView4);

        // Set the welcome message with the client's name
        if (clientName != null && !clientName.isEmpty()) {
            welcomeTextView.setText("Hi, " + clientName);
        } else {
            welcomeTextView.setText("Hi, Guest");
        }

        // Find the LinearLayout views for the services
        LinearLayout service1Layout = findViewById(R.id.Service1);
        LinearLayout service2Layout = findViewById(R.id.Service2);
        LinearLayout service3Layout = findViewById(R.id.Service3);
        LinearLayout StaffLayout = findViewById(R.id.StaffPage);
        LinearLayout AppointmentsLayout = findViewById(R.id.AppointmentsPage);
        //LinearLayout ProfileLayout = findViewById(R.id.ProfilePage);
        Button logoutButton = findViewById(R.id.button4);

        // Set click listeners for the service LinearLayouts
        service1Layout.setOnClickListener(v -> {
            // Handle click for Service1
            navigateToBookPage(clientId, "Haircut");
        });

        service2Layout.setOnClickListener(v -> {
            // Handle click for Service2
            navigateToBookPage(clientId, "Hair Dye");
        });

        service3Layout.setOnClickListener(v -> {
            // Handle click for Service3
            navigateToBookPage(clientId, "Makeup");
        });

        StaffLayout.setOnClickListener(v -> {
            // Handle click for Staff
            Intent staffIntent = new Intent(HomePageF.this, StaffPage.class);
            staffIntent.putExtra("clientId", clientId);
            startActivity(staffIntent);
        });

        AppointmentsLayout.setOnClickListener(v -> {
            // Handle click for Appointment
            Intent appointmentsIntent = new Intent(HomePageF.this, AppointmentF.class);
            appointmentsIntent.putExtra("clientId", clientId);
            startActivity(appointmentsIntent);
        });

        /*ProfileLayout.setOnClickListener(v -> {
            // Handle click for Profile
            Intent profileIntent = new Intent(HomePageF.this, Profile.class);
            startActivity(profileIntent);
        });*/


     logoutButton.setOnClickListener(v -> {
         // Intent to start MainActivity
         Intent mainActivityIntent = new Intent(HomePageF.this, MainActivity.class);
         // Clear the back stack so the user can't navigate back to the HomePageF after logging out
         mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(mainActivityIntent);
     });
}
    // Method to navigate to the book page activity
    private void navigateToBookPage(int clientId, String serviceName) {
        Intent intent = new Intent(HomePageF.this, book.class);
        intent.putExtra("clientId", clientId);
        intent.putExtra("serviceName", serviceName);
        startActivity(intent);
    }

    private String getClientName(int clientId) {
        DBHelper dbHelper = new DBHelper(this);
        String clientName = dbHelper.getClientName(clientId);
        dbHelper.close();
        return clientName;
    }
}
