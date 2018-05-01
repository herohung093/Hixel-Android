package com.hixel.hixel.dashboard;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View {

    private DashboardContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard);

        ActivityDashboardBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        // Setup Toolbar
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.toolbarTitle.setText(R.string.dashboard);

        showMainGraph();
    }

    @Override
    public void setPresenter(@NonNull DashboardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMainGraph() {

    }
}
