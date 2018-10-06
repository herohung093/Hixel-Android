package com.hixel.hixel.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import com.hixel.hixel.R;

public class UpdatePasswordActivity extends AppCompatActivity {
    TextInputLayout newPassTV, confirmPassTV;
    Button changePassButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        changePassButton= findViewById(R.id.changePassButton);
        newPassTV= findViewById(R.id.forgotView_PassWrapper);
        confirmPassTV= findViewById(R.id.forgotView_ConfirmPassWrapper);

        changePassButton.setOnClickListener(event->{
            if (validate()) {
                Toast.makeText(getBaseContext(), "Your password has been updated",
                    Toast.LENGTH_LONG + 1).show();
                Intent moveToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(moveToLogin);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        if (newPassTV.getEditText().getText().toString()
            .compareTo(confirmPassTV.getEditText().getText().toString())!=0){
            valid=false;
            Toast.makeText(getBaseContext(), "Your passwords do not match", Toast.LENGTH_LONG).show();
        }

        if (newPassTV.getEditText().getText().toString().length()<4){
            valid=false;
            newPassTV.setError("Must contain at least 4 characters");
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
