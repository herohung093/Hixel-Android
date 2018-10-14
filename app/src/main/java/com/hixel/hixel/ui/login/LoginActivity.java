package com.hixel.hixel.ui.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;
import com.hixel.hixel.App;
import com.hixel.hixel.R;
import com.hixel.hixel.dashboard.DashboardActivity;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.models.LoginData;
import com.hixel.hixel.databinding.ActivityLoginBinding;
import com.hixel.hixel.login.ForgotPasswordActivity;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        setupUI();
    }

    private void setupUI() {

        loginButton.setOnClickListener(view -> {
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

        String email = emailText.getEditText().getText().toString();
        String password = passwordText.getEditText().getText().toString();

        Call<Void> call = Client.getClient()
                .create(ServerInterface.class)
                .login(new LoginData(email, password));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                    @NonNull Response<Void> response) {
                progressDialog.dismiss();
                switch (response.code()) {
                    case 200:
                        SharedPreferences preferences = App.preferences();

                        preferences.edit()
                                .putString("AUTH_TOKEN", response.headers().get("Authorization"))
                                .apply();

                        preferences.edit()
                                .putString("REFRESH_TOKEN", response.headers().get("Refresh"))
                                .apply();

                        onLoginSuccess();
                        break;
                    case 401:
                        onLoginFailed("Wrong username or password");
                        break;
                    default:
                        onLoginFailed("Unknown error");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                onLoginFailed("Couldn't connect to server!");
            }
        });
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
