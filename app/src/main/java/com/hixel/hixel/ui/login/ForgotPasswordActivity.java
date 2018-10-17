package com.hixel.hixel.ui.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityForgotPasswordBinding;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

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
            // TODO: show notification and get respond from server here
            if (!validate()) {
                Toast.makeText(getBaseContext(),
                        "Invalid email address! Try again", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "check your email for further information",
                    Toast.LENGTH_LONG + 3).show();
                onSendCodeSuccess();
            }
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
    }

    private boolean validate() {
        String email = emailIdText.getEditText().getText().toString().trim();

        if (!viewModel.isValidEmail(email)) {
            emailIdText.setError("Invalid email address");
            return false;
        }

        return true;
    }

    public void onSendCodeSuccess() {
        Intent moveToPinInput = new Intent(this, PinInputActivity.class);
        startActivity(moveToPinInput);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    /* TODO: Check if this is needed
    public String getEmail() {
        EditText editText = emailIdText.getEditText();

        if (editText == null)
            return "";

        return editText.getText().toString().trim();
    }*/
}
