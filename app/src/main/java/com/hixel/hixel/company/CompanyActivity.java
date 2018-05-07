package com.hixel.hixel.company;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.hixel.hixel.R;
import com.hixel.hixel.dashboard.DashboardContract;
import com.hixel.hixel.data.Company;
import com.hixel.hixel.databinding.ActivityDashboardBinding;

public class CompanyActivity extends AppCompatActivity implements CompanyContract.View {

    private CompanyContract.Presenter presenter;
    private ActivityDashboardBinding binding;
    private String TAG = "COMPANY_VIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        presenter = new CompanyPresenter(this);
        presenter.setCompany((Company) getIntent().getSerializableExtra("company"));
        presenter.start();

        //binding = DataBindingUtil.setContentView(this, R.layout.activity_company);
        //setSupportActionBar(binding.toolbar.toolbar);
        //binding.toolbar.toolbarTitle.setText(presenter.getCompanyName());
    }

    public void setPresenter(@NonNull CompanyContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
