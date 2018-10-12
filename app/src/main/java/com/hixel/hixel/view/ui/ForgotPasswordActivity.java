package com.hixel.hixel.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.service.network.ServerInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hixel.hixel.service.network.Client.getClient;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputLayout emailIdText;
    Button backButton, submitButton;
    TextView hint_TV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailIdText = findViewById(R.id.registerEmailWrapper);
        backButton = findViewById(R.id.btn_backToLogin);
        submitButton = findViewById(R.id.btn_submit);
        hint_TV = findViewById(R.id.textView4);

        backButton.setOnClickListener(event->{
            Intent moveToLogin= new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(moveToLogin);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        });

        submitButton.setOnClickListener(event->{
            //TODO: show notification and get respond from server here
            if (!validate()){
                Toast.makeText(getBaseContext(), "Invalid email address! Try again", Toast.LENGTH_LONG).show();
            }
            else {
                Call<Void> call = getClient()
                        .create(ServerInterface.class)
                        .resetEmail(getEmail());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        switch (response.code()) {
                            case 200:
                                Toast.makeText(getBaseContext(), "Check your email now!",
                                        Toast.LENGTH_LONG + 3).show();
                                onSendCodeSuccess();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(getBaseContext(), "Couldn't connect to server!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    private boolean validate() {
        boolean valid = true;

        String email = getEmail();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailIdText.setError("Invalid email address");
            valid = false;
        }
        else {
            emailIdText.setError(null);
        }

        return valid;
    }
    public void onSendCodeSuccess(){
        Intent moveToPinInput = new Intent(this,PinInputActivity.class);
        moveToPinInput.putExtra("PASSWORD_RESET_EMAIL", getEmail());
        startActivity(moveToPinInput);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public String getEmail()
    {
        EditText editText = emailIdText.getEditText();

        if (editText == null)
            return "";

        return editText.getText().toString().trim();
    }
}
