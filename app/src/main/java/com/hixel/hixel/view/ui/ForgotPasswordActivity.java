package com.hixel.hixel.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import com.hixel.hixel.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputLayout emailIdText;
    Button backButton, submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailIdText = findViewById(R.id.registerEmailWrapper);
        backButton = findViewById(R.id.btn_backToLogin);
        submitButton= findViewById(R.id.btn_submit);

        backButton.setOnClickListener(event->{
            Intent moveToLogin= new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(moveToLogin);
        });

        submitButton.setOnClickListener(event->{
            //TODO: show notification and get respond from server here

            Toast.makeText(getBaseContext(), "check your email for further information", Toast.LENGTH_LONG).show();
        });
    }
    private boolean validate() {
        boolean valid = true;

        String email = emailIdText.getEditText().getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailIdText.setError("Invalid email address");
            valid = false;
        }
        else {
            emailIdText.setError(null);
        }

        return valid;
    }
}
