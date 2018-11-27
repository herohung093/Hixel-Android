package com.hixel.hixel.ui.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.databinding.ActivityUpdatePasswordBinding;

import java.util.Objects;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

public class UpdatePasswordActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginViewModel viewModel;
    private String email;
    private String pin;

    ActivityUpdatePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_password);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        email = (String) Objects.requireNonNull(extras).getSerializable("PASSWORD_RESET_EMAIL");
        pin = (String) Objects.requireNonNull(extras).getSerializable("PASSWORD_RESET_PIN");


        binding.changePassButton.setOnClickListener(event -> {
            if (validate(getPasswordString(), getConfirmPasswordString())) {

                Call<Void> call = Client.getClient()
                        .create(ServerInterface.class)
                        .resetPassword(email, pin, getPasswordString());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        switch (response.code()) {
                            case 200:
                                Toast.makeText(getBaseContext(), "Your password has been updated", Toast.LENGTH_SHORT).show();
                                break;

                            case 401:
                                Toast.makeText(getBaseContext(), "Pin expired: Try again!", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        Intent moveToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(moveToLogin);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(getBaseContext(), "Couldn't connect to server!", Toast.LENGTH_LONG).show();
                    }
                });
            }
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

    private String getPasswordString() {
        EditText editText = binding.forgotViewPassWrapper.getEditText();

        return (editText != null) ? editText.getText().toString() : "";
    }

    private String getConfirmPasswordString() {
        EditText editText = binding.forgotViewConfirmPassWrapper.getEditText();

        return (editText != null) ? editText.getText().toString() : "";
    }

    public boolean validate(String first, String second) {
        boolean valid = true;

        if (!first.equals(second)) {
            valid = false;
            Toast.makeText(getBaseContext(),
                    "Your passwords do not match", Toast.LENGTH_LONG).show();
        }

        if (!viewModel.isValidPassword(first)) {
            valid = false;
            binding.forgotViewPassWrapper.setError("Must contain at least 4 characters");
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
