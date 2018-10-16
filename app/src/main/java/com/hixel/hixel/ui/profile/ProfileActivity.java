package com.hixel.hixel.ui.profile;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.hixel.hixel.R;
import com.hixel.hixel.ui.companycomparison.CompanyComparisonActivity;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.databinding.ActivityProfileBinding;
import com.hixel.hixel.ui.login.LoginActivity;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

/**
 * Displays the users profile information, and allows them to alter that information.
 */
// TODO: Rename SecondName to LastName
public class ProfileActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ProfileViewModel viewModel;

    private ActivityProfileBinding binding;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        binding.toolbar.toolbar.setTitle(R.string.profile);
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE);
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(binding.toolbar.toolbar);

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
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProfileViewModel.class);
        viewModel.init();
        viewModel.getUser().observe(this, this::updateUI);
    }

    /**
     * Method takes in a User and sets up UI with the users information
     * @param user The current user
     */
    public void updateUI(User user) {
        if (user != null) {

            binding.confirmEditFirstNameButton.setVisibility(View.INVISIBLE);
            binding.confirmEditSecondNameButton.setVisibility(View.INVISIBLE);

            String header = String.format("Hi, %s %s!", user.getFirstName(), user.getLastName());
            binding.fullName.setText(header);

            binding.firstName.setText(user.getFirstName());
            binding.secondName.setText(user.getLastName());

            String passwordDummy = "12345";
            binding.password.setText(passwordDummy);

            binding.firstName.setFocusable(false);
            binding.secondName.setFocusable(false);
            binding.password.setFocusable(false);
        }
    }

    public void setupBottomNavigationView() {
        /*
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
        });*/
    }

    // TODO: Need API endpoints to update user first and last name.
    public void setupEditButtons() {
        binding.editFirstNameButton.setOnClickListener(view -> {
            binding.firstName.setFocusableInTouchMode(true);

            binding.editFirstNameButton.setVisibility(View.INVISIBLE);
            binding.confirmEditFirstNameButton.setVisibility(View.VISIBLE);

            binding.confirmEditFirstNameButton.setOnClickListener(view2 -> {
                // viewModel.updateUserFirstName(binding.firstName.getText().toString());
                binding.editFirstNameButton.setVisibility(View.VISIBLE);
                binding.confirmEditFirstNameButton.setVisibility(View.INVISIBLE);
            });
        });

        binding.editSecondNameButton.setOnClickListener(view -> {
            binding.secondName.setFocusableInTouchMode(true);

            binding.editSecondNameButton.setVisibility(View.INVISIBLE);
            binding.confirmEditSecondNameButton.setVisibility(View.VISIBLE);

            binding.confirmEditSecondNameButton.setOnClickListener(view2 -> {
                // viewModel.updateUserSecondName(binding.secondName.getText().toString());
                binding.editSecondNameButton.setVisibility(View.VISIBLE);
                binding.confirmEditSecondNameButton.setVisibility(View.INVISIBLE);
            });
        });

        binding.editPasswordButton.setOnClickListener(view -> setupChangePasswordPopup());
    }

    /**
     * Method logs user out of the application.
     */
    public void setupLogout() {
        binding.logoutButton.setOnClickListener(view -> {
            Intent moveToLogin = new Intent(this, LoginActivity.class);
            startActivity(moveToLogin);
        });
    }

    /**
     * Method sets up a dialog popup to allow the user to change their password.
     */
    public void setupChangePasswordPopup() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_password_popup);

        ImageButton close = dialog.findViewById(R.id.close_dialog_button);
        close.setOnClickListener(view2 -> dialog.dismiss());

        Button confirm = dialog.findViewById(R.id.confirm_new_password_button);

        confirm.setOnClickListener(view -> updatePassword());

        dialog.show();
    }

    /**
     * Method updates the users password
     */
    public void updatePassword() {
        EditText oldPassword = dialog.findViewById(R.id.old_password_edit_text);
        EditText newPassword = dialog.findViewById(R.id.new_password_edit_text);
        EditText retypedPassword = dialog.findViewById(R.id.retype_new_edit_text);

        String old = oldPassword.getText().toString();
        String first = newPassword.getText().toString();
        String second = retypedPassword.getText().toString();

        if (viewModel.isValidPassword(first, second)) {
            viewModel.updateUserPassword(old, first);
        } else {
            displaySnackbar("Error with passwords. Try again.");
        }
    }

    /**
     * Method displays a snackbar message to the UI
     * @param message The message to display
     */
    public void displaySnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchView = menu.findItem(R.id.action_search);
        searchView.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
