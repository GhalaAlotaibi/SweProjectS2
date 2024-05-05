package com.example.sweprojects2;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText birthdayEditText;
    private EditText passwordEditText;
    private EditText passwordEditText2;
    private Button signupButton;
    private Button loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_signup);

        // Initialize views
        nameEditText = findViewById(R.id.editTextText5);
        emailEditText = findViewById(R.id.editTextText11);
        phoneNumberEditText = findViewById(R.id.editTextText22);
        birthdayEditText = findViewById(R.id.editTextText55);
        passwordEditText = findViewById(R.id.editTextText2);
        passwordEditText2 = findViewById(R.id.editTextText3);
        signupButton = findViewById(R.id.button2);
        loginbutton = findViewById(R.id.button3);

        signupButton.setOnClickListener(v -> {
            // Get user input
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();
            String birthday = birthdayEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String passwordConfirmation = passwordEditText2.getText().toString();

            // Check if any field is empty
            if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || birthday.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
                Toast.makeText(signup.this, "Please enter all the required information.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(signup.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate phone number format
            if (!isValidPhoneNumber(phoneNumber)) {
                Toast.makeText(signup.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate birthday format (assuming MM/dd/yyyy format)
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(birthday);
            } catch (ParseException e) {
                Toast.makeText(signup.this, "Please enter a valid date of birth (MM/dd/yyyy).", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(password)) {
                Toast.makeText(signup.this, "Please enter a valid password (at least 8 characters including a number).", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!password.equals(passwordConfirmation)) {
                Toast.makeText(signup.this, "Please make sure the password fields match.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform signup operation
            performSignup(name, email, phoneNumber, birthday, password);
        });

        loginbutton.setOnClickListener(v -> {
            Intent intent = new Intent(signup.this, login.class);
            startActivity(intent);
        });
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Regex pattern to match phone number format (with optional leading + sign, optional country code, and at least 10 digits)
        String phoneNumberPattern = "^\\+?[0-9]{10,}$";
        return Pattern.compile(phoneNumberPattern).matcher(phoneNumber).matches();
    }
    private boolean isValidPassword(String password) {
        // Regex pattern to match password format (at least 8 characters including a number)
        String passwordPattern = "^(?=.*[0-9]).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }
    private void performSignup(String name, String email, String phoneNumber, String birthday, String password) {
        DBHelper dbHelper = new DBHelper(signup.this);

        // Check if email already exists
        if (dbHelper.isEmailExists(email)) {
            Toast.makeText(signup.this, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the client
        boolean result = dbHelper.addClient(name, email, phoneNumber, birthday, password);

        if (result){
            Toast.makeText(signup.this, "Client registered successfully", Toast.LENGTH_SHORT).show();
            // Redirect to home activity
            Intent intent = new Intent(signup.this, HomePageF.class);
            int clientId = dbHelper.getClientId(email);
            intent.putExtra("clientId", clientId);
            startActivity(intent);
            finish();
        }
        else
            Toast.makeText(signup.this, "Client registration failed", Toast.LENGTH_SHORT).show();
    }
}
