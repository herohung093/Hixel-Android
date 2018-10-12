package com.hixel.hixel.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.service.network.ServerInterface;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hixel.hixel.service.network.Client.getClient;

public class UpdatePasswordActivity extends AppCompatActivity {
    TextInputLayout newPassTV, confirmPassTV;
    Button changePassButton;
    String email;
    String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        changePassButton = findViewById(R.id.changePassButton);
        newPassTV = findViewById(R.id.forgotView_PassWrapper);
        confirmPassTV = findViewById(R.id.forgotView_ConfirmPassWrapper);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        email = (String) Objects.requireNonNull(extras).getSerializable("PASSWORD_RESET_EMAIL");
        pin = (String) Objects.requireNonNull(extras).getSerializable("PASSWORD_RESET_PIN");

        changePassButton.setOnClickListener((View event) -> {
            if (validate()) {
                Call<Void> call = getClient()
                        .create(ServerInterface.class)
                        .resetPassword(email, pin, getNewPassword());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        switch (response.code()) {
                            case 200:
                                Toast.makeText(getBaseContext(), "Password updated", Toast.LENGTH_SHORT).show();
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
    }
    public boolean validate() {
        boolean valid = true;

        String newPass = getNewPassword();
        String confirmPass = getConfirmPassword();

        if (newPass.compareTo(confirmPass) != 0){
            valid = false;
            Toast.makeText(getBaseContext(), "Your passwords do not match", Toast.LENGTH_LONG).show();
        }

        if (newPass.length() < 4){
            valid = false;
            newPassTV.setError("Must contain at least 4 characters");
        }

        return valid;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public String getNewPassword() {
        EditText editText = newPassTV.getEditText();

        if (editText == null)
            return null;

        return editText.getText().toString();
    }

    public String getConfirmPassword() {
        EditText editText = confirmPassTV.getEditText();

        if (editText == null)
            return null;

        return editText.getText().toString();
    }
}
