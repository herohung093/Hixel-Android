package com.hixel.hixel.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.login.LoginActivity;

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
        submitButton= findViewById(R.id.btn_submit);
        hint_TV = findViewById(R.id.textView4);

        backButton.setOnClickListener(event->{
            Intent moveToLogin= new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(moveToLogin);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        });

        submitButton.setOnClickListener(event->{
            //TODO: show notification and get respond from server here
            if(!validate()){
                Toast.makeText(getBaseContext(), "Invalid email address! Try again", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getBaseContext(), "check your email for further information",
                    Toast.LENGTH_LONG + 3).show();
                onSendCodeSuccess();
            }
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
    public void onSendCodeSuccess(){
        Intent moveToPininput = new Intent(this, com.hixel.hixel.view.ui.PinInputActivity.class);
        startActivity(moveToPininput);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
