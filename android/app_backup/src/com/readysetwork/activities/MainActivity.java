.package com.readysetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.readysetwork.R;
import com.readysetwork.services.NetworkService;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button postJobButton, viewJobsButton, profileButton, logoutButton;
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        welcomeText = findViewById(R.id.welcomeText);
        postJobButton = findViewById(R.id.postJobButton);
        viewJobsButton = findViewById(R.id.viewJobsButton);
        profileButton = findViewById(R.id.profileButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Initialize NetworkService for user data
        networkService = new NetworkService();

        // Welcome text, dynamically fetch the user name if available
        welcomeText.setText("Welcome, " + getUserName());

        // Set onClick listeners for each button
        postJobButton.setOnClickListener(v -> openPostJobActivity());
        viewJobsButton.setOnClickListener(v -> openJobsListActivity());
        profileButton.setOnClickListener(v -> openUserProfile());
        logoutButton.setOnClickListener(v -> performLogout());
    }

    /**
     * Retrieve the logged-in user's name from shared preferences or other stored data.
     * This is just a placeholder, replace with actual data retrieval.
     */
    private String getUserName() {
        // Replace with actual user retrieval logic (e.g., from SharedPreferences, database, etc.)
        return "User";
    }

    /**
     * Opens the activity where the user can post a new job.
     */
    private void openPostJobActivity() {
        Intent intent = new Intent(MainActivity.this, PostJobActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the activity where the user can view available jobs.
     */
    private void openJobsListActivity() {
        Intent intent = new Intent(MainActivity.this, JobsListActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the user's profile screen.
     */
    private void openUserProfile() {
        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Logs out the user, clearing any session or authentication tokens.
     */
    private void performLogout() {
        // Clear user session or token (Example: Clear SharedPreferences)
        networkService.logout(new NetworkService.LogoutCallback() {
            @Override
            public void onSuccess() {
                showToast("Successfully logged out");
                navigateToLogin();
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast("Error logging out: " + errorMessage);
            }
        });
    }

    /**
     * Navigate to the login screen after logout.
     */
    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();  // Close this activity so user can't return to it via back button
    }

    /**
     * Utility method to show Toast messages.
     * @param message the message to be shown in the Toast
     */
    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}