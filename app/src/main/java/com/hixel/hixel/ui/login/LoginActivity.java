package com.hixel.hixel.ui.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.dashboard.DashboardActivity;
import com.hixel.hixel.databinding.ActivityLoginBinding;
import com.hixel.hixel.login.ForgotPasswordActivity;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginViewModel viewModel;

    private ActivityLoginBinding binding;
    TextInputLayout emailText;
    TextInputLayout passwordText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPreferences.getBoolean(Onboarding.COMPLETED_ONBOARDING_PREF_NAME, false)) {
            startActivity(new Intent(this, Onboarding.class));
        }

        loginButton = findViewById(R.id.btn_login);
        emailText = findViewById(R.id.emailWrapper);
        passwordText = findViewById(R.id.passwordWrapper);

        this.configureDagger();
        this.configureViewModel();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
        viewModel.init();

        // TODO: Fix up the Room query because this doesn't make sense.
        if (!viewModel.getIsUserStale()) {
            setupUI();
        } else {
            Log.d(TAG, "configureViewModel: user ain't stale");
            Intent moveToDashboard = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(moveToDashboard);
        }
    }

    private void setupUI() {

        loginButton.setOnClickListener(view -> {
            Log.d(TAG, "setupUI: login button clicked");
            login();
        });

        binding.linkSignup.setOnClickListener(view -> {
            Intent moveToSignup = new Intent(getApplicationContext(),SignupActivity.class);
            startActivity(moveToSignup);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

        binding.linkForgotPassword.setOnClickListener(event-> {
            Intent moveToForgotView = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(moveToForgotView);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

    }

    private void login() {
        Log.d(TAG, "login: HERE!");
        if (!validate()) {
            onLoginFailed("Invalid input");
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();

        if (viewModel.login(email, password)) {
            Log.d(TAG, "login: LOGIN SUCCESS!");
            onLoginSuccess();
        } else {
            onLoginFailed("TODO: GET THIS FUNCTIONALITY BACK!");
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void onLoginSuccess() {
        loginButton.setEnabled(true);
        Intent moveToDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(moveToDashboard);
        finish();
    }

    private void onLoginFailed(String reason) {
        Toast.makeText(getBaseContext(), "Login failed: " + reason, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        // TODO: Get rid of these null pointer errors.
        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();

        Log.d(TAG, "validate: " + email);

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Invalid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordText.setError("Must contain at least 4 characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
