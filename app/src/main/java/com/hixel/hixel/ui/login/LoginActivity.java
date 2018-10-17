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
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;
import com.hixel.hixel.App;
import com.hixel.hixel.R;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.models.LoginData;
import com.hixel.hixel.databinding.ActivityLoginBinding;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for logging into the application. User enters their email and password
 * which is passed to the server and verified.
 */
public class LoginActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginViewModel viewModel;

    private ActivityLoginBinding binding;
    private TextInputLayout emailText;
    private TextInputLayout passwordText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Checks to see if Onboarding should be launched.
        if (!sharedPreferences.getBoolean(Onboarding.COMPLETED_ONBOARDING_PREF_NAME, false)) {
            startActivity(new Intent(this, Onboarding.class));
        }

        loginButton = findViewById(R.id.btn_login);
        emailText = findViewById(R.id.emailWrapper);
        passwordText = findViewById(R.id.passwordWrapper);

        this.configureDagger();
        this.configureViewModel();
    }

    /**
     * Method configures dependency injection for the class
     */
    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    /**
     * Method instantiates the ViewModel and begins UI setup.
     */
    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
        setupUI();
    }

    /**
     * Method sets up UI elements
     */
    private void setupUI() {
        loginButton.setOnClickListener(view -> login());

        binding.linkSignup.setOnClickListener(view -> {
            Intent moveToSignup = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(moveToSignup);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

        binding.linkForgotPassword.setOnClickListener(event -> {
            Intent moveToForgotView =
                    new Intent(getApplicationContext(), ForgotPasswordActivity.class);

            startActivity(moveToForgotView);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

    }

    /**
     * Method takes the users email and password, validates the strings and if valid
     * verifies that the user exists on the server
     */
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

    /**
     * Method disables the ability to return to the previous Activity.
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Method moves to the DashboardActivity.class and destroys the LoginActivity.class
     */
    private void onLoginSuccess() {
        loginButton.setEnabled(true);
        Intent moveToDashboard = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(moveToDashboard);
        finish();
    }

    /**
     * Method displays a Toast informing the user as to why the login failed.
     *
     * @param reason String to input into Toast
     */
    private void onLoginFailed(String reason) {
        Toast.makeText(getBaseContext(), "Login failed: " + reason, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    /**
     * Method validates whether the user entered email and password are valid
     *
     * @return a boolean indicating if the email and password are valid.
     */
    private boolean validate() {
        boolean valid = true;

        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();

        if (!viewModel.isValidEmail(email)) {
            emailText.setError("Invalid email address");
            valid = false;
        }

        if (!viewModel.isValidPassword(password)) {
            passwordText.setError("Must contain at least 4 characters");
            valid = false;
        }

        return valid;
    }
}
