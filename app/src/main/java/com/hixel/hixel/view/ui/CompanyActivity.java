package com.hixel.hixel.view.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import com.hixel.hixel.R;
import com.hixel.hixel.data.CompanyEntity;
import com.hixel.hixel.viewmodel.CompanyViewModel;
import com.hixel.hixel.databinding.ActivityCompanyBinding;
import dagger.android.AndroidInjection;
import javax.inject.Inject;


public class CompanyActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = CompanyActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CompanyViewModel viewModel;

    FloatingActionButton fab;
    ActivityCompanyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company);

        String ticker = getIntent().getStringExtra("COMPANY_TICKER");

        Log.d(TAG, "Ticker: " + ticker);

        this.configureDagger();
        this.configureViewModel(ticker);
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel(String ticker) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CompanyViewModel.class);
        viewModel.loadCompany(ticker);

        if (viewModel.getCompany() != null) {
            viewModel.getCompany().observe(this, this::updateUI);
        }
    }

    private void updateUI(CompanyEntity company) {

        if (company != null) {
            setupBottomNavigationView();

            // Setup the toolbar
            String title = company.getIdentifiers().getName();
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
                viewModel.saveCompany();
                setResult(RESULT_OK, backIntent);
                finish();
            });

            if (viewModel.companyIsInPortfolio()) {
                fab.setVisibility(View.INVISIBLE);
            }

            companyChartSetup();
            setupProgressPercentage();
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

    private void companyChartSetup() {
        PieView pieView = findViewById(R.id.company_pie);

        pieView.setMainBackgroundColor(ContextCompat.getColor(this, R.color.shaded));
        pieView.setTextColor(ContextCompat.getColor(this, R.color.text_main_light));
        pieView.setMainBackgroundColor(ContextCompat.getColor(this, R.color.secondary_background));
        pieView.setPieInnerPadding(20);

        viewModel.getCompany().observe(this, company -> {
            pieView.setPercentage((float) (company.getRatio() + 80));
            pieView.setPercentageBackgroundColor(getColorIndicator((int) (company.getRatio() + 80)));
        });

        PieAngleAnimation animation = new PieAngleAnimation(pieView);
        animation.setDuration(1500);

        pieView.startAnimation(animation);
    }

    private void setupProgressPercentage() {
        viewModel.getCompany().observe(this, company -> {
            binding.healthProgress.setProgress((int) (company.getRatio() + 15));
            binding.healthProgress.getProgressDrawable().setTint(getColorIndicator((int) (company.getRatio() + 15)));

            binding.performanceProgress.setProgress((int) (company.getRatio() + 25));
            binding.performanceProgress.getProgressDrawable().setTint(getColorIndicator((int) (company.getRatio() + 25)));

            binding.riskProgress.setProgress((int) (company.getRatio() + 5018531));
            binding.riskProgress.getProgressDrawable().setTint(getColorIndicator((int) (company.getRatio() + 50)));

            binding.strengthProgress.setProgress((int) (company.getRatio() + 75));
            binding.strengthProgress.getProgressDrawable().setTint(getColorIndicator((int) (company.getRatio() + 75)));

            binding.returnProgress.setProgress((int) (company.getRatio() + 85));
            binding.returnProgress.getProgressDrawable().setTint(getColorIndicator((int) (company.getRatio() + 85)));
        });
    }

    private int getColorIndicator(int value) {
        if (value < 50) {
            return ContextCompat.getColor(this, R.color.bad);
        } else if (value > 60) {
            return ContextCompat.getColor(this, R.color.good);
        }

        return ContextCompat.getColor(this, R.color.average);
    }

}
