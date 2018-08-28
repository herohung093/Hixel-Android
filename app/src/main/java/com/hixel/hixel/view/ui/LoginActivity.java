package com.hixel.hixel.view.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hixel.hixel.MyApp;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.ApplicationUser;
import com.hixel.hixel.service.models.LoginData;
import com.hixel.hixel.service.network.ServerInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hixel.hixel.service.network.Client.getClient;

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
            onLoginFailed("Invalid input");
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

        Call<Void> call = getClient()
                .create(ServerInterface.class)
                .login(new LoginData(email, password));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                progressDialog.dismiss();
                switch (response.code()) {
                    case 200:
                        SharedPreferences preferences = MyApp.preferences();
            Log.e("Successful OnResponse headers: ", response.headers().toString());
                        preferences.edit()
                                .putString("AUTH_TOKEN", response.headers().get("Authorization"))
                                .apply();

                        preferences.edit()
                                .putString("REFRESH_TOKEN", response.headers().get("Refresh"))
                                .apply();

                        onLoginSuccess();
                        break;
                    case 401:
                        onLoginFailed("Wrong username or password");
                        break;
                    default:
                        onLoginFailed("Unknown error");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                onLoginFailed("Couldn't connect to server!");
            }
        });
    }
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void onLoginSuccess() {
        loginButton.setEnabled(true);
        Intent moveToDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(moveToDashboard);
        finish();
    }

    private void onLoginFailed(String reason) {
        Toast.makeText(getBaseContext(), "Login failed: " + reason, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Invalid email address");
            valid = false;
        }
        else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordText.setError("Must contain at least 4 characters");
            valid = false;
        }
        else {
            passwordText.setError(null);
        }

        return valid;
    }
}
