package com.hixel.hixel.view.ui;

import static com.hixel.hixel.service.network.Client.getClient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.ApplicationUser;
import com.hixel.hixel.service.network.ServerInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    TextInputLayout emailText,passwordText,firstNameText,lasNameText;
    TextView loginText;
    Button signupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        emailText = findViewById(R.id.signup_EmailWrapper);
        passwordText=findViewById(R.id.signup_PassWrapper);
        firstNameText= findViewById(R.id.firstNameWrapper);
        lasNameText= findViewById(R.id.lastnameWrapper);
        loginText = findViewById(R.id.link_login);
        signupButton= findViewById(R.id.btn_signup);

        signupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                signup();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent moveToLogin= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(moveToLogin);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    private void signup() {
        if (!validate()) {
            onSignupFailed("Invalid input");
            return;
        }

        signupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstName = firstNameText.getEditText().getText().toString().trim();
        String lastName = lasNameText.getEditText().getText().toString().trim();
        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();

        Call<Void> call = getClient()
                .create(ServerInterface.class)
                .signup(new ApplicationUser(firstName, lastName, email, password));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        onSignupSuccess();
                        progressDialog.dismiss();
                        break;

                    case 409:
                        onSignupFailed("Email is already in use");
                        break;

                    case 500:
                        onSignupFailed("Invalid input");
                        break;

                    default:
                        onSignupFailed("Unknown error: Code " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                onSignupFailed("Couldn't connect to server!");
                progressDialog.dismiss();
            }
        });
    }

    public void onSignupFailed(String reason) {
        Toast.makeText(getBaseContext(), "Signup failed: " + reason, Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }
    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        finish();
    }
    public boolean validate() {
        boolean valid = true;

        String firstName = firstNameText.getEditText().getText().toString();
        String lastName = lasNameText.getEditText().getText().toString();
        String email = emailText.getEditText().getText().toString();
        String password = passwordText.getEditText().toString();

        if (firstName.isEmpty()) {
            firstNameText.setError("Name can't be empty!");
            valid = false;
        }
        else {
            firstNameText.setError(null);
        }
        if (lastName.isEmpty()) {
            lasNameText.setError("Name can't be empty!");
            valid = false;
        }
        else {
            lasNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Invalid email address");
            valid = false;
        }
        else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 ) {
            passwordText.setError("Must contain at least 4 characters");
            valid = false;
        }
        else {
            passwordText.setError(null);
        }

        return valid;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}


