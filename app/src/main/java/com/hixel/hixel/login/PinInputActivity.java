package com.hixel.hixel.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.goodiebag.pinview.Pinview;
import com.hixel.hixel.R;

public class PinInputActivity extends AppCompatActivity {

    Pinview pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_input);

        Intent moveToUpdatePassword = new Intent(this, UpdatePasswordActivity.class);
        pin = new Pinview(this);
        pin = findViewById(R.id.pinview);

        pin.setPinViewEventListener((view, bool) -> {
            // TODO: get pin values by pin.getValue() to verify
            startActivity(moveToUpdatePassword);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
