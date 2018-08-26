package com.hixel.hixel.view.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.hixel.hixel.R;

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
                Intent moveToLogin= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(moveToLogin);
            }
        });
    }

    private void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
    }
    signupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstName=firstNameText.getEditText().getText().toString().trim();
        String lastName=lasNameText.getEditText().getText().toString().trim();
        String email= emailText.getEditText().getText().toString().trim();
        String password= passwordText.getEditText().toString().trim();
        //TODO: do something here
        Intent moveToDashboard = new Intent(this,DashboardActivity.class);
        startActivity(moveToDashboard);
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    // On complete call either onSignupSuccess or onSignupFailed 
                    // depending on success
                    onSignupSuccess();
                    // onSignupFailed();
                    progressDialog.dismiss();
                }
            }, 3000);
    }


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }
    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        finish();
    }
    public boolean validate() {
        boolean valid = true;

        String firstName=firstNameText.getEditText().getText().toString();
        String lastName=lasNameText.getEditText().getText().toString();
        String email= emailText.getEditText().getText().toString();
        String password= passwordText.getEditText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            firstNameText.setError(null);
        }
        if (lastName.isEmpty() || lastName.length() < 3) {
            lasNameText.setError("at least 3 characters");
            valid = false;
        } else {
            lasNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 ) {
            passwordText.setError("password must contain at least 4 characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }
//TODO: check email existence here
        return valid;
    }
}


