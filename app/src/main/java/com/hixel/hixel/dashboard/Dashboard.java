package com.hixel.hixel.dashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.hixel.hixel.R;

public class Dashboard extends AppCompatActivity implements DashboardContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }
}
