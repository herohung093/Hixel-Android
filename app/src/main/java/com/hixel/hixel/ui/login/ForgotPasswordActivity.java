package com.hixel.hixel.ui.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.entities.user.User;
import com.hixel.hixel.databinding.ActivityForgotPasswordBinding;
import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

/**
 * Presents the UI for a user to request a new password.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginViewModel viewModel;

    private TextInputLayout emailIdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityForgotPasswordBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_forgot_password);

        emailIdText = binding.registerEmailWrapper;

        binding.btnBackToLogin.setOnClickListener(event -> {
            Intent moveToLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(moveToLogin);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        });

        binding.btnSubmit.setOnClickListener(event -> {
            if (!validate()) {
                Toast.makeText(getBaseContext(),
                        "Invalid email address! Try again", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Check your email for further information.",
                        Toast.LENGTH_LONG + 3).show();

                Call<Void> call = Client.getClient()
                        .create(ServerInterface.class)
                        .resetEmail(getEmailString());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        switch (response.code()) {
                            case 200:
                                Toast.makeText(getBaseContext(), "Check your email now!",
                                        Toast.LENGTH_LONG + 3).show();
                                onSendCodeSuccess();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(getBaseContext(), "Network error: Try again", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        binding.inputRegisteredEmail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.btnSubmit.performClick();
                return true;
            }
            return false;
        });


        configureDagger();
        configureViewModel();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
    }

    private String getEmailString() {
        EditText editText = emailIdText.getEditText();

        return (editText != null) ? editText.getText().toString().trim() : "";
    }

    private boolean validate() {
        String email = getEmailString();

        if (!viewModel.isValidEmail(email)) {
            emailIdText.setError("Invalid email address");
            return false;
        }

        return true;
    }

    public void onSendCodeSuccess() {
        Intent moveToPinInput = new Intent(this, PinInputActivity.class);
        moveToPinInput.putExtra("PASSWORD_RESET_EMAIL", getEmailString());

        startActivity(moveToPinInput);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
