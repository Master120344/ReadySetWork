package com.readysetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.readysetwork.R;
import com.readysetwork.services.NetworkService;
import com.readysetwork.utils.Utility;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button loginButton;
    private TextView forgotPasswordText, signUpPromptText;
    private ImageButton eyeToggleButton;
    private boolean isPasswordVisible = false;
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        signUpPromptText = findViewById(R.id.signUpPromptText);
        eyeToggleButton = findViewById(R.id.eyeToggleButton);

        // Initialize NetworkService for login request
        networkService = new NetworkService();

        // Handle password visibility toggle
        eyeToggleButton.setOnClickListener(v -> togglePasswordVisibility());

        // Handle login button click
        loginButton.setOnClickListener(v -> performLogin());

        // Handle forgot password click
        forgotPasswordText.setOnClickListener(v -> openForgotPasswordActivity());

        // Handle sign up prompt click
        signUpPromptText.setOnClickListener(v -> openSignUpActivity());

        // Input validation for email and password
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Initial validation check for input fields
        validateInputs();
    }

    /**
     * Toggles the password visibility when the eye icon is clicked.
     */
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            editTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eyeToggleButton.setImageResource(R.drawable.ic_eye_off);
        } else {
            // Show password
            editTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eyeToggleButton.setImageResource(R.drawable.ic_eye_on);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    /**
     * Perform login validation and submit to the network service if valid.
     */
    private void performLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (isInputValid(email, password)) {
            // Proceed with login API request
            networkService.login(email, password, new NetworkService.LoginCallback() {
                @Override
                public void onSuccess(String userToken) {
                    // Proceed to next screen on successful login
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Display error message
                    showToast(errorMessage);
                }
            });
        } else {
            showToast("Please enter valid credentials.");
        }
    }

    /**
     * Validates the email and password input fields.
     * @param email the email entered by the user
     * @param password the password entered by the user
     * @return true if valid, false otherwise
     */
    private boolean isInputValid(String email, String password) {
        if (email.isEmpty() || !Utility.isValidEmail(email)) {
            editTextEmail.setError("Invalid email address");
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    /**
     * Validates whether the inputs are valid for enabling/disabling the login button.
     */
    private void validateInputs() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Enable login button only if inputs are valid
        loginButton.setEnabled(!email.isEmpty() && !password.isEmpty() && Utility.isValidEmail(email) && password.length() >= 6);
    }

    /**
     * Opens the ForgotPasswordActivity.
     */
    private void openForgotPasswordActivity() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the SignUpActivity.
     */
    private void openSignUpActivity() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    /**
     * Utility method to show Toast messages.
     * @param message the message to be shown in the Toast
     */
    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}