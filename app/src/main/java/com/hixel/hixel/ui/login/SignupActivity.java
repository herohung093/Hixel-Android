package com.hixel.hixel.ui.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.models.ApplicationUser;
import com.hixel.hixel.databinding.ActivitySignupBinding;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginViewModel viewModel;

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        binding.btnSignup.setOnClickListener(view -> signup());

        binding.linkLogin.setOnClickListener(view -> {
            Intent moveToLogin= new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(moveToLogin);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
    }

    private void signup() {
        String firstName = binding.firstNameWrapper.getEditText().getText().toString().trim();
        String lastName = binding.lastnameWrapper.getEditText().getText().toString().trim();
        String email = binding.signupEmailWrapper.getEditText().getText().toString().trim();
        String password = binding.signupPassWrapper.getEditText().getText().toString().trim();

        if (!validate(firstName, lastName, email, password)) {
            onSignupFailed("Invalid input");
            return;
        }

        binding.btnSignup.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        Call<Void> call = Client.getClient()
                .create(ServerInterface.class)
                .signup(new ApplicationUser(firstName, lastName, email, password));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        onSignupSuccess();
                        progressDialog.dismiss();
                        break;
                    case 409:
                        onSignupFailed("Email is already in use");
                        break;
                    case 500:
                        onSignupFailed("Invalid input");
                        break;
                    default:
                        onSignupFailed("Unknown error: Code " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                onSignupFailed("Couldn't connect to server!");
                progressDialog.dismiss();
            }
        });
    }

    public void onSignupFailed(String reason) {
        Toast.makeText(getBaseContext(), "Signup failed: " + reason, Toast.LENGTH_LONG).show();
        binding.btnSignup.setEnabled(true);
    }

    public void onSignupSuccess() {
        binding.btnSignup.setEnabled(true);
        finish();
    }

    public boolean validate(String firstName, String lastName, String email, String password) {
        boolean valid = true;

        if (!viewModel.isValidName(firstName)) {
            binding.firstNameWrapper.setError("Name can't be empty!");
            valid = false;
        }

        if (!viewModel.isValidName(lastName)) {
            binding.lastnameWrapper.setError("Name can't be empty!");
        }


        if (viewModel.isValidEmail(email)) {
            binding.signupEmailWrapper.setError("Invalid email address");
            valid = false;
        }

        if (viewModel.isValidPassword(password)) {
            binding.signupPassWrapper.setError("Must contain at least 4 characters");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}


