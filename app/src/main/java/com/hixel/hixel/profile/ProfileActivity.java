package com.hixel.hixel.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // TODO: Get data from db
        String fullName = "Laxman Marothiya";
        String email = "john1993@hixel.com.au";

        binding.fullName.setText(fullName);
    }
}
