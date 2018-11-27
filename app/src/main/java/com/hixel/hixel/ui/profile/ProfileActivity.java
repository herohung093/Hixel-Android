package com.hixel.hixel.ui.profile;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hixel.hixel.R;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.ui.base.BaseActivity;
import com.hixel.hixel.data.entities.user.User;
import com.hixel.hixel.databinding.ActivityProfileBinding;
import com.hixel.hixel.ui.login.LoginActivity;
import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import javax.inject.Inject;

/**
 * Displays the users profile information, and allows them to alter that information.
 */
// TODO: Rename SecondName to LastName
public class ProfileActivity extends BaseActivity<ActivityProfileBinding>  {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ProfileViewModel viewModel;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_profile);

        setupToolbar(R.string.profile, true, false);

        this.configureDagger();
        this.configureViewModel();

        setupBottomNavigationView(R.id.profile_button);

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

    /**
     * Initializes onClickListeners for buttons in the view.
     */
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
            viewModel.deleteUserData();
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

        if (!first.equals(second)) {
            displayToast("Your passwords do not match.");
            return;
        }

        if (first.length() < 4) {
            displayToast("Passwords must be 4 characters or more.");
            return;
        }

        Call<Void> call = Client.getClient()
                                .create(ServerInterface.class)
                                .changePassword(old, first);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        Timber.d("UPDATED");
                        displayToast("Your password has been updated.");
                        dialog.dismiss();
                        break;

                    case 401:
                        Timber.d("NOT UPDATED");
                        displayToast("Current Password was incorrect.");
                        break;
                }
            }


            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                displayToast("Couldn't connect to server!");
            }
        });
    }

    /**
     * Method displays a snackbar message to the UI
     * @param message The message to display
     */
    public void displayToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
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
