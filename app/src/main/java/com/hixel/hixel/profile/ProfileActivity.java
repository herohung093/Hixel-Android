package com.hixel.hixel.profile;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.hixel.hixel.R;
import com.hixel.hixel.companycomparison.CompanyComparisonActivity;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.databinding.ActivityProfileBinding;
import com.hixel.hixel.ui.login.LoginActivity;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class ProfileActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ProfileViewModel viewModel;
    private ActivityProfileBinding binding;

    // TODO: Get data from db.
    String fullName = "John Smith";
    String email = "test@gmail.com";
    String password = "1234";

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        this.configureDagger();
        this.configureViewModel();

        setupBottomNavigationView();
        setupEditButtons();
        setupLogout();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel.class);
        viewModel.init();
        viewModel.getUser().observe(this, this::updateUI);
    }


    public void updateUI(User user) {
        if (user != null) {
            Log.d(TAG, "updateUI: HIT!");
            String userHeader = String.format("Hi, %s!", user.getFirstName());

            binding.confirmEditNameButton.setVisibility(View.INVISIBLE);
            binding.confirmEditEmailButton.setVisibility(View.INVISIBLE);

            binding.fullName.setText(userHeader);
            binding.name.setText(user.getFirstName());
            binding.email.setText(user.getEmail());
            binding.password.setText(user.getPassword());

            binding.name.setFocusable(false);
            binding.email.setFocusable(false);
            binding.password.setFocusable(false);
        }
    }

    public void setupBottomNavigationView() {
        binding.bottomNavigation.bottomNavigation.setSelectedItemId(R.id.profile_button);
        binding.bottomNavigation.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToHome = new Intent(this, DashboardActivity.class);
                    startActivity(moveToHome);
                    break;
                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this, CompanyComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;
            }

            return true;
        });
    }

    public void setupEditButtons() {
        binding.editNameButton.setOnClickListener(view -> {
            binding.name.setFocusableInTouchMode(true);

            binding.confirmEditNameButton.setVisibility(View.VISIBLE);

            binding.confirmEditNameButton.setOnClickListener(view2 -> {
                fullName = binding.name.getText().toString();
            });
        });

        binding.editEmailButton.setOnClickListener(view -> {
            binding.email.setFocusableInTouchMode(true);

            binding.confirmEditEmailButton.setVisibility(View.VISIBLE);

            binding.confirmEditEmailButton.setOnClickListener(view2 -> {
                email = binding.email.getText().toString();
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
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_password_popup);

        ImageButton close = dialog.findViewById(R.id.close_dialog_button);
        close.setOnClickListener(view2 -> dialog.dismiss());

        Button confirm = dialog.findViewById(R.id.confirm_new_password_button);

        confirm.setOnClickListener(view -> updatePassword());

        dialog.show();
    }


    public void updatePassword() {
        if (isValidPassword()) {
            EditText newPassword = findViewById(R.id.retype_new_edit_text);
            password = newPassword.getText().toString();
        }
    }

    // TODO: Better error handling and databinding.
    public boolean isValidPassword() {
        boolean isValid = false;

        EditText oldPassword = findViewById(R.id.old_password_edit_text);
        EditText newPassword = findViewById(R.id.new_password_edit_text);
        EditText retypedPassword = findViewById(R.id.retype_new_edit_text);

        if (oldPassword.getText().toString().equals(password)) {
            if (newPassword.getText().toString().equals(retypedPassword.getText().toString())) {
                isValid = true;
            } else {
                displaySnackbar("New Passwords do not match");
            }
        } else {
            displaySnackbar("Current password is incorrect");
        }

        return isValid;
    }

    public void displaySnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
    }
}
