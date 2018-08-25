package com.hixel.hixel.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.viewmodel.CompanyViewModel;
import java.util.ArrayList;
import java.util.Objects;
import com.hixel.hixel.databinding.ActivityCompanyBinding;


public class CompanyActivity extends AppCompatActivity {

    CompanyViewModel companyViewModel;
    FloatingActionButton fab;
    ActivityCompanyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company);

        companyViewModel = ViewModelProviders.of(this).get(CompanyViewModel.class);

        setupBottomNavigationView();
        companyViewModel.setupSearch();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Company company = (Company) Objects.requireNonNull(extras).getSerializable("CURRENT_COMPANY");
        companyViewModel.setCompany(company);

        ArrayList<Company> companies = (ArrayList<Company>) extras.getSerializable("PORTFOLIO");


        // Setup the toolbar
        String title = companyViewModel.getCompany().getValue().getIdentifiers().getName();
        binding.toolbar.toolbar.setTitle(title);
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE);

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_close);

        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Setup FAB
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent backIntent = getIntent();
            backIntent.putExtra("COMPANY_ADD", company);
            setResult(RESULT_OK,backIntent);
            finish();
        });

        if (companies != null) {
            for (Company c : companies) {
                if (c.getIdentifiers().getName().equals(companyViewModel.getCompany().getValue().getIdentifiers().getName())) {
                    fab.setVisibility(View.INVISIBLE);
                 }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToDashBoard = new Intent(this, DashboardActivity.class);
                    startActivity(moveToDashBoard);
                    break;
                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this, ComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;
                case R.id.settings_button:
                    // This screen is yet to be implemented
                    break;
            }

            return true;
        });
    }


}
