package com.hixel.hixel.ui.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityUpdatePasswordBinding;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class UpdatePasswordActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginViewModel viewModel;

    ActivityUpdatePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_password);

        String first = binding.forgotViewPassWrapper.getEditText().getText().toString();
        String second = binding.forgotViewConfirmPassWrapper.getEditText().getText().toString();

        binding.changePassButton.setOnClickListener(event->{
            if (validate(first, second)) {
                Toast.makeText(getBaseContext(), "Your password has been updated",
                    Toast.LENGTH_LONG + 1).show();
                Intent moveToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(moveToLogin);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
    }

    public boolean validate(String first, String second) {
        boolean valid = true;

        if (viewModel.validatePasswordUpdate(first, second)){
            valid = false;
            Toast.makeText(getBaseContext(), "Your passwords do not match", Toast.LENGTH_LONG).show();
        }

        if (viewModel.isValidPassword(first)){
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
