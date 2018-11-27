package com.hixel.hixel.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.hixel.hixel.R;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinInputActivity extends AppCompatActivity {

    Pinview pin;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_input);

        Intent moveToUpdatePassword = new Intent(this, UpdatePasswordActivity.class);
        pin = new Pinview(this);
        pin = findViewById(R.id.pinview);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        email = (String) Objects.requireNonNull(extras).getSerializable("PASSWORD_RESET_EMAIL");

        pin.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean b) {
                Call<Void> call = Client.getClient()
                        .create(ServerInterface.class)
                        .resetCode(email, pin.getValue());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        switch (response.code()) {
                            case 200:
                                Intent moveToUpdatePassword = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("PASSWORD_RESET_PIN", pin.getValue());
                                extras.putString("PASSWORD_RESET_EMAIL", email);
                                moveToUpdatePassword.putExtras(extras);

                                startActivity(moveToUpdatePassword);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                break;

                            case 401:
                                Toast.makeText(getBaseContext(), "Wrong pin: Try again.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
