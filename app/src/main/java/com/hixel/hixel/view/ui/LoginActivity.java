package com.hixel.hixel.view.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.hixel.hixel.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    TextInputLayout emailText;
    TextInputLayout passwordText;
    Button loginButton;
    TextView signupLink;
    int REQUEST_SIGNUP = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = (TextInputLayout) findViewById(R.id.emailWrapper);
        passwordText= (TextInputLayout) findViewById(R.id.passwordWrapper);
        loginButton= (Button) findViewById(R.id.btn_login);
        signupLink = (TextView) findViewById(R.id.link_signup);

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                Intent moveToSignUp = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(moveToSignUp);
            }
        });
        signupLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
            Intent moveToSignup = new Intent(getApplicationContext(),SignupActivity.class);
            startActivity(moveToSignup);
            }
        });
    }

    private void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getEditText().getText().toString();
        String password = passwordText.getEditText().getText().toString();

        // TODO: Implement  authentication logic here.

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    // On complete call either onLoginSuccess or onLoginFailed
                    onLoginSuccess();
                    // onLoginFailed();
                    progressDialog.dismiss();
                }
            }, 3000);
    }
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordText.setError("password must contain at least 4 characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
