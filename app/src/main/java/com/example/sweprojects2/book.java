package com.example.sweprojects2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class book extends AppCompatActivity {

    private TextView serviceNameText;
    private RecyclerView staffRecyclerView, timeSlotRecyclerView;
    private Button selectDateButton, bookAppointmentButton;
    private TextView selectDateText;

    private DBHelper dbHelper;
    private int clientId;
    private String selectedService;
    private String selectedDate;
    private Staff selectedStaff;
    private String selectedTimeSlot;
    private int appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_book);

        // Initialize views
        serviceNameText = findViewById(R.id.serviceNameText);
        staffRecyclerView = findViewById(R.id.staffRecyclerView);
        timeSlotRecyclerView = findViewById(R.id.timeSlotRecyclerView);
        selectDateButton = findViewById(R.id.selectDateButton);
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);
        selectDateText = findViewById(R.id.selectDateText);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);
        LinearLayout StaffLayout = findViewById(R.id.StaffPage);
        LinearLayout HomeLayout = findViewById(R.id.HomePage);
        LinearLayout AppointmentsLayout = findViewById(R.id.AppointmentsPage);

        // Get clientId and selectedService from Intent
        Intent intent = getIntent();
        clientId = intent.getIntExtra("clientId", -1);
        selectedService = intent.getStringExtra("serviceName"); // Assuming you pass the service name
        appointmentId = intent.getIntExtra("appointmentId", -1); // Get appointment ID
        int staffId = intent.getIntExtra("staffId", -1);
        selectedDate = intent.getStringExtra("date");
        selectedTimeSlot = intent.getStringExtra("time");

        // Set service name
        serviceNameText.setText(selectedService);

        // Load staff list based on selected service
        loadStaffList();

        // Set up date picker
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateSelectedDate(calendar);
            }
        };

        selectDateButton.setOnClickListener(v -> {
            Calendar currentDate = Calendar.getInstance();

            // Create date picker dialog with minimum date set to current date
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    book.this,
                    dateSetListener,
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
            datePickerDialog.show();
        });

        // Set up time slot RecyclerView
        timeSlotRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        loadTimeSlots();

        bookAppointmentButton.setOnClickListener(v -> {
            if (selectedStaff != null && selectedDate != null && selectedTimeSlot != null) {
                bookAppointment();
            } else {
                Toast.makeText(this, "Please select staff, date, and time slot", Toast.LENGTH_SHORT).show();
            }
        });

        StaffLayout.setOnClickListener(v -> {
            // Handle click for Staff
            Intent staffIntent = new Intent(book.this, StaffPage.class);
            staffIntent.putExtra("clientId", clientId);
            startActivity(staffIntent);
        });

        HomeLayout.setOnClickListener(v -> {
            // Handle click for Appointment
            Intent appointmentsIntent = new Intent(book.this, HomePageF.class);
            appointmentsIntent.putExtra("clientId", clientId);
            startActivity(appointmentsIntent);
        });

        AppointmentsLayout.setOnClickListener(v -> {
            // Handle click for Profile
            Intent appointmentsIntent = new Intent(book.this, AppointmentF.class);
            appointmentsIntent.putExtra("clientId", clientId);
            startActivity(appointmentsIntent);
        });
    }

    private void updateSelectedDate(Calendar calendar) {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        selectedDate = sdf.format(calendar.getTime());
        selectDateText.setText("Selected Date: " + selectedDate);
        loadTimeSlots(); // Reload time slots for the selected date
    }

    private void loadStaffList() {
        List<Staff> staffList = dbHelper.getStaffBySpecialty(selectedService);
        StaffAdapter staffAdapter = new StaffAdapter(staffList, this::onStaffSelected);
        staffRecyclerView.setAdapter(staffAdapter);
        staffRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void onStaffSelected(Staff staff) {
        selectedStaff = staff;
    }

    private void loadTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        // Generate time slots between 12 PM and 8 PM
        for (int hour = 12; hour <= 20; hour++) {
            timeSlots.add(String.format("%02d:00 %s", hour % 12, hour < 12 ? "AM" : "PM"));
        }
        TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(timeSlots, this::onTimeSlotSelected);
        timeSlotRecyclerView.setAdapter(timeSlotAdapter);
    }

    private void onTimeSlotSelected(String timeSlot) {
        selectedTimeSlot = timeSlot;
    }

    private void bookAppointment() {

        if (appointmentId != -1) {
            // Edit mode
            bookO updatedAppointment = new bookO(appointmentId, clientId, selectedStaff.getStaffId(), selectedDate, selectedTimeSlot, selectedStaff.getStaffName(), selectedService);
            boolean isUpdated = dbHelper.updateAppointment(updatedAppointment);
            if (isUpdated) {
                Toast.makeText(this, "Appointment Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AppointmentF.class);
                intent.putExtra("clientId", clientId);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to update appointment", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            // Create a new bookO object with the selected details
            bookO newAppointment = new bookO(
                    -1, // appointmentID (auto-incremented)
                    clientId,
                    selectedStaff.getStaffId(),
                    selectedDate,
                    selectedTimeSlot,
                    selectedStaff.getStaffName(),
                    selectedService
            );
            // Add the appointment to the database
            boolean isAdded = dbHelper.addAppointment(newAppointment);

            if (isAdded) {
                Toast.makeText(this, "Appointment booked!", Toast.LENGTH_SHORT).show();
                // Navigate back to the home page or appointments page
                // Example:
                Intent intent = new Intent(this, AppointmentF.class);
                intent.putExtra("clientId", clientId);
                startActivity(intent);
                finish(); // Finish the current activity (optional)
            } else {
                Toast.makeText(this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
