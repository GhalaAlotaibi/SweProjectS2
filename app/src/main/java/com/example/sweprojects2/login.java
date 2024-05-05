package com.example.sweprojects2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this); // Instantiate DBHelper

        emailEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.editTextText2);
        loginButton = findViewById(R.id.button2);
        signupButton = findViewById(R.id.button3);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login.this, "Please enter all the required information.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call the loginClient method
                boolean loggedIn = dbHelper.loginClient(email, password);

                if (loggedIn) {
                    // Successful login
                    Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    // Redirect to home activity
                    Intent intent = new Intent(login.this, HomePageF.class);
                    // Pass the client ID to the home activity
                    int clientId = dbHelper.getClientId(email);
                    intent.putExtra("clientId", clientId);
                    startActivity(intent);
                    finish();
                } else {
                    // Invalid login credentials
                    Toast.makeText(login.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });
    }
}
