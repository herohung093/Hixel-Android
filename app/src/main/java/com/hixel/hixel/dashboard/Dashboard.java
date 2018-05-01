package com.hixel.hixel.dashboard;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextClock;
import android.widget.TextView;

import com.hixel.hixel.R;

public class Dashboard extends AppCompatActivity implements DashboardContract.View {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setupToolbar();
    }

    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText(R.string.dashboard);
    }
}
