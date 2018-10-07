package com.hixel.hixel.profile;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityProfileBinding;
import com.hixel.hixel.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    // TODO: Get data from db.
    String fullName = "John Smith";
    String email = "test@gmail.com";
    String password = "1234";

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        updateUI();
        setupEditButtons();
        setupLogout();
    }

    public void updateUI() {
        String userHeader = String.format("Hi, %s!", fullName);

        binding.confirmEditNameButton.setVisibility(View.INVISIBLE);
        binding.confirmEditEmailButton.setVisibility(View.INVISIBLE);


        binding.fullName.setText(userHeader);
        binding.name.setText(fullName);
        binding.email.setText(email);
        binding.password.setText(password);

        binding.name.setFocusable(false);
        binding.email.setFocusable(false);
        binding.password.setFocusable(false);
    }

    public void setupEditButtons() {
        binding.editNameButton.setOnClickListener(view -> {
            binding.name.setFocusableInTouchMode(true);

            binding.confirmEditNameButton.setVisibility(View.VISIBLE);

            binding.confirmEditNameButton.setOnClickListener(view2 -> {
                fullName = binding.name.getText().toString();
                updateUI();
            });
        });

        binding.editEmailButton.setOnClickListener(view -> {
            binding.email.setFocusableInTouchMode(true);

            binding.confirmEditEmailButton.setVisibility(View.VISIBLE);

            binding.confirmEditEmailButton.setOnClickListener(view2 -> {
                email = binding.email.getText().toString();
                updateUI();
            });
        });

        binding.editPasswordButton.setOnClickListener(view -> setupChangePasswordPopup());
    }

    public void setupLogout() {
        binding.logoutButton.setOnClickListener(view -> {
            Intent moveToLogin = new Intent(this, LoginActivity.class);
            startActivity(moveToLogin);
        });
    }

    public void setupChangePasswordPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_password_popup);

        Button close = dialog.findViewById(R.id.close_dialog_button);
        close.setOnClickListener(view2 -> dialog.dismiss());
    }
}
