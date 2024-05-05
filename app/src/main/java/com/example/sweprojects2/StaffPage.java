package com.example.sweprojects2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

public class StaffPage extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_staffpage);

        Intent intent = getIntent();
        int clientId = intent.getIntExtra("clientId", -1);

        dbHelper = new DBHelper(this);
        LinearLayout AppointmentsLayout = findViewById(R.id.AppointmentsPage);
        LinearLayout HomeLayout = findViewById(R.id.HomePage);
        //LinearLayout ProfileLayout = findViewById(R.id.ProfilePage);

        HomeLayout.setOnClickListener(v -> {
            // Handle click for Staff
            Intent homeIntent = new Intent(StaffPage.this, HomePageF.class);
            homeIntent.putExtra("clientId", clientId);
            startActivity(homeIntent);
        });

        AppointmentsLayout.setOnClickListener(v -> {
            // Handle click for Appointment
            Intent appointmentsIntent = new Intent(StaffPage.this, AppointmentF.class);
            appointmentsIntent.putExtra("clientId", clientId);
            startActivity(appointmentsIntent);
        });

        /*ProfileLayout.setOnClickListener(v -> {
            // Handle click for Profile
            Intent profileIntent = new Intent(StaffPage.this, Profile.class);
            startActivity(profileIntent);
        });*/

        // Load staff data and populate UI elements
        loadStaffData();
    }

    private void loadStaffData() {
        List<Staff> staffList = dbHelper.getAllStaff();

        // Get references to UI elements
        ImageView staff1Image = findViewById(R.id.pic22);
        TextView staff1NameText = findViewById(R.id.name1);
        TextView staff1SpecialtyText = findViewById(R.id.speciality1);

        ImageView staff2Image = findViewById(R.id.pic32);
        TextView staff2NameText = findViewById(R.id.name332);
        TextView staff2SpecialtyText = findViewById(R.id.name32);

        ImageView staff3Image = findViewById(R.id.pic2);
        TextView staff3NameText = findViewById(R.id.name52);
        TextView staff3SpecialtyText = findViewById(R.id.name552);

        // Populate UI elements with staff data
        if (!staffList.isEmpty()) {
            // Staff 1
            Staff staff1 = staffList.get(0);
            Glide.with(this).load(R.drawable.staff1).into(staff1Image);
            staff1NameText.setText(staff1.getStaffName());
            staff1SpecialtyText.setText(staff1.getSpecialty());

            // Staff 2
            if (staffList.size() > 1) {
                Staff staff2 = staffList.get(1);
                Glide.with(this).load(R.drawable.staff3).into(staff2Image);
                staff2NameText.setText(staff2.getStaffName());
                staff2SpecialtyText.setText(staff2.getSpecialty());
            }

            // Staff 3
            if (staffList.size() > 2) {
                Staff staff3 = staffList.get(2);
                Glide.with(this).load(R.drawable.staff5).into(staff3Image);
                staff3NameText.setText(staff3.getStaffName());
                staff3SpecialtyText.setText(staff3.getSpecialty());
            }
        }
    }
}
